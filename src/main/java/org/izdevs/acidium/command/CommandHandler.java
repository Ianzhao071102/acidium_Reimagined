package org.izdevs.acidium.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.izdevs.acidium.serialization.NBTParser.registerNBTDef;

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
            registerNBTDef(is);
        }catch(Throwable e){
            throw new RuntimeException(e);
        }
        return "success";
    }
}