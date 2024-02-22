package org.izdevs.acidium.command;

import org.izdevs.acidium.serialization.YamlParser;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;

@ShellComponent
public class CommandHandler {
//    EXAMPLE
//    @ShellMethod(key = "hello-world")
//    public String helloWorld(@ShellOption(defaultValue = "spring") String arg) {
//        return "Hello world " + arg;
//    }

    @ShellMethod(key = "regClassPathYaml")
    public String regClassPathYaml(@ShellOption(defaultValue = "name") String name) throws FileNotFoundException {
       Path url = null;
        try {
            url = Paths.get(String.valueOf(this.getClass().getResource(name)));
        }catch(Throwable e){
            throw new RuntimeException(e);
        }

        File file = new File(url.toString());
        YamlParser.registerYamlDef(new FileInputStream(file));
        return "successful";
    }
}