package org.izdevs.acidium.serialization;

import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.izdevs.acidium.serialization.parsers.JSONParser;
import org.izdevs.acidium.utils.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Service("default_locator")
public class DefaultResourcesLocator implements ResourceLocator {
    @Autowired
    JSONParser parser;

    @Override
    public URI getURI(String namespace, String typeName, String name) {
        //todo finish this
        return null;
    }

    @Override
    public ResourceSchema[] locate() {

        try {
            ResourceSchema[] schema = new ResourceSchema[ReflectUtil.getJSONResources().length];
            AtomicInteger i = new AtomicInteger();
            Arrays.stream(ReflectUtil.getJSONResources()).iterator().forEachRemaining((a) -> {
                try {
                    ResourceSchema resource = parser.deserialize(a.getInputStream());
                    schema[i.get()] = resource;
                    i.getAndIncrement();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return schema;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
