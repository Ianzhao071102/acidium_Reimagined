package org.izdevs.acidium.command;

import org.izdevs.acidium.serialization.YamlParser;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.izdevs.acidium.serialization.YamlParser.registerYamlDef;

@ShellComponent
public class CommandHandler {
//    EXAMPLE
//    @ShellMethod(key = "hello-world")
//    public String helloWorld(@ShellOption(defaultValue = "spring") String arg) {
//        return "Hello world " + arg;
//    }

    @ShellMethod(key = "regClassPathYaml")
    public String regClassPathYaml(@ShellOption(defaultValue = "name") String name) throws FileNotFoundException {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(name);
            registerYamlDef(is);
        }catch(Throwable e){
            throw new RuntimeException(e);
        }
        return "success";
    }
}