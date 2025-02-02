package org.izdevs.acidium.serialization;


import org.izdevs.acidium.serialization.models.ResourceSchema;
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
                logger.debug(r.getURI().getPath());
                pool.execute(() -> {
                    facade.registerResource(decode(r, DeserializerTypes.JSON));
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ResourceSchema decode(Resource resource, DeserializerTypes type) {
        try {
            InputStream stream = resource.getInputStream();
            return factory.getDeserializer(type).deserialize(stream);
        }catch(IOException e){
            logger.error("failed to parse resource",e);
        }
        throw new RuntimeException("unknown exception, check the code");
    }
}
