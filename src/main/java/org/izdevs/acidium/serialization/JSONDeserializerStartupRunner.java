package org.izdevs.acidium.serialization;

import org.izdevs.acidium.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class JSONDeserializerStartupRunner implements ApplicationRunner {
    @Autowired
    ResourceFacade facade;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SerializerFactory factory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource[] resources = ReflectUtil.getJSONResources();
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (Resource r : resources) {
            //ignore any null values
            //fuck NullPointerException.class
            if(r == null) break;
            try {
                pool.execute(() -> {
                    try {
                        facade.registerResource(decode(r.getURI(), DeserializerTypes.JSON));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private org.izdevs.acidium.serialization.Resource decode(URI uri, DeserializerTypes type) {
        StringBuilder a = new StringBuilder();
        Path path;
        try {
            final Map<String, String> env = new HashMap<>();
            final String[] array = uri.toString().split("!");
            final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
            path = fs.getPath(array[1]);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            List<String> lines = Files.readAllLines(path);
            boolean emit = lines.size() >= 10;
            int it = 0;
            logger.debug("----- BEGIN JSON DATA -----");
            for (String s : lines) {
                a.append(s);
                a.append(System.lineSeparator());
                if(it < 10){
                    logger.debug(s);
                    it++;
                }
            }
            if(emit){
                logger.debug(String.format("%s lines has been emitted",lines.size() -10));
            }
            logger.debug("----- END JSON DATA -----");

            if (lines.isEmpty()) {
                throw new RuntimeException("failed to read json, unexpected length: 0");
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to deserialize json, failed to read lines of uri:" + uri.getPath());
        }
        return factory.getDeserializer(type).deserialize(a.toString());
    }
}
