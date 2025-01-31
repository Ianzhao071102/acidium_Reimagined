package org.izdevs.acidium.serialization.parsers;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.serialization.DeserializerTypes;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceDeserializer;
import org.izdevs.acidium.serialization.annotations.ResourceSchemaDefinition;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Service
@Component
@Slf4j
public class JSONParser extends ResourceDeserializer {
    @Autowired
    Set<ResourceSchema> schemas;
    String err_msg = "";

    @PostConstruct
    public void writeErrMSG() {
        Set<String> types = new HashSet<>();
        for (ResourceSchema schema : schemas) {
            types.add(schema.getClass().getTypeName());
        }
        err_msg = new Gson().toJson(types);
    }

    @Override
    public ResourceSchema deserialize(String data) {
        Gson gson = new Gson();
        Resource stp1 = gson.fromJson(data, Resource.class);

        for (ResourceSchema rs : schemas) {
            if (rs.getClass().getAnnotation(ResourceSchemaDefinition.class).value().equals(stp1.getTypeName())) {
                Class<? extends ResourceSchema> _class = rs.getClass();

                return new Gson().fromJson(data,_class);
            }
        }

        log.warn("failed to deserialize string data, supported typename(s) are:{}, skipping", err_msg);
        throw new RuntimeException("failed to deserialize string data, supported typename(s) are:" + err_msg);
    }

    @Override
    public ResourceSchema deserialize(InputStream input) {
        InputStreamReader reader = new InputStreamReader(input);
        Resource stp1 = new Gson().fromJson(reader, Resource.class);

        for (ResourceSchema rs : schemas) {
            if (rs.getClass().getAnnotation(ResourceSchemaDefinition.class).value().equals(stp1.getTypeName())) {
                Class<? extends ResourceSchema> _class = rs.getClass();

                return new Gson().fromJson(new InputStreamReader(input),_class);
            }
        }

        log.error("failed to deserialize json data, supported typename(s) are:{}, skipping", err_msg);
        throw new RuntimeException("deserialize json data failed");
    }

    @Override
    public <T> ResourceSchema deserialize(InputStream input, T type) {
        InputStreamReader reader = new InputStreamReader(input);
        return (ResourceSchema) new Gson().fromJson(reader, type.getClass());
    }

    public JSONParser() {
        super(DeserializerTypes.JSON);
    }
}
