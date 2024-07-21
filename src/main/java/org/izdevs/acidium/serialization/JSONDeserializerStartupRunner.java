package org.izdevs.acidium.serialization;

import com.yworks.common.ResourcePolicy;
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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
                    facade.registerResource(decode(r, DeserializerTypes.JSON));
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private org.izdevs.acidium.serialization.Resource decode(Resource resource, DeserializerTypes type) {
        try {
            InputStream stream = resource.getInputStream();
            return factory.getDeserializer(type).deserialize(stream);
        }catch(IOException e){
            logger.error("failed to parse resource",e);
        }
        throw new RuntimeException("unknown exception, check the code");
    }
}
