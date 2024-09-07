package org.izdevs.acidium.command;

import jakarta.validation.constraints.NotNull;
import org.izdevs.acidium.configuration.Config;
import org.izdevs.acidium.serialization.parsers.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ShellComponent
public class BasicCommands {

    @Autowired
    JSONParser parser;
    @ShellMethod(value = "get version data", key = "version")
    public String version(){
      return Config.version();
    }
  
    @ShellMethod(value = "get credits", key = "credits")
    public String credits(){
      return Config.getCredits();
    }
    @ShellMethod(value = "regClassPathYaml")
    public String regClassPathYaml(@ShellOption(defaultValue = "name") String name) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(name);
            parser.deserialize(is);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    @ShellMethod(value = "note")
    public String notes(@ShellOption(defaultValue = "ALL") String noteName) {
        if (Objects.equals(noteName, "ALL")) {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            try {
                org.springframework.core.io.Resource[] metaInfResources = resourcePatternResolver
                        .getResources("classpath*:*.note");
                for (org.springframework.core.io.Resource r : metaInfResources) {
                    List<String> lines = Files.readAllLines(Path.of(r.getURI()));
                    StringBuilder builder = getStringBuilder(lines);
                    return builder.toString();
                }
                return "No Notes Are Found......";
            } catch (Throwable e) {
                throw new RuntimeException("error when getting the files..." + Arrays.toString(e.getStackTrace()));
            }
        } else {
            return getString(noteName);
        }
    }

    @NotNull
    private String getString(String noteName) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            org.springframework.core.io.Resource[] metaInfResources = resourcePatternResolver
                    .getResources("classpath*:*.note");
            for (org.springframework.core.io.Resource r : metaInfResources) {
                if (Objects.requireNonNull(r.getFilename()).equalsIgnoreCase(noteName)) {
                    List<String> lines = Files.readAllLines(Path.of(r.getURI()));
                    final StringBuilder builder = getStringBuilder(lines);


                    return builder.toString();
                }
            }
            return "No Notes Are Found......";
        } catch (Throwable e) {
            throw new RuntimeException("error when getting the files..." + Arrays.toString(e.getStackTrace()));
        }
    }

    private static StringBuilder getStringBuilder(List<String> lines) {
        StringBuilder builder = new StringBuilder();

        builder.append("------------- BEGIN ------------");
        builder.append(System.lineSeparator());

        for (int i = 0; i <= lines.size() - 1; i++) {
            builder.append(lines.get(i));
            builder.append(System.lineSeparator());
        }

        builder.append("------------- END ---------------");
        builder.append(System.lineSeparator());
        return builder;
    }

    
}