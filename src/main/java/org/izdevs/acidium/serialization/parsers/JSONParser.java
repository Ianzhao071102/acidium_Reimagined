package org.izdevs.acidium.serialization.parsers;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.serialization.DeserializerTypes;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceDeserializer;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

@Service
@Component
@Slf4j
public class JSONParser extends ResourceDeserializer {
    @Autowired
    Set<ResourceSchema> schemas;
    String err_msg = "";

    @PostConstruct
    public void writeErrs() {
        err_msg = new Gson().toJson(schemas);
    }

    @Override
    public ResourceSchema deserialize(String data) {
        Gson gson = new Gson();
        Resource stp1 = gson.fromJson(data, Resource.class);

        for (ResourceSchema rs : schemas) {
            if (rs.getClass().getSimpleName().equalsIgnoreCase(stp1.getTypeName())) {
                Class<? extends ResourceSchema> _class = rs.getClass();

                return new Gson().fromJson(data,_class);
            }
        }

        log.error("failed to deserialize, supported typename(s) are:" + err_msg);
        throw new UnsupportedOperationException("invalid schema definition, cannot find ResourceSchema for the specified typename:" + stp1.typeName);
    }

    @Override
    public ResourceSchema deserialize(InputStream input) {
        InputStreamReader reader = new InputStreamReader(input);
        Resource stp1 = new Gson().fromJson(reader, Resource.class);

        for (ResourceSchema rs : schemas) {
            if (rs.getClass().getSimpleName().equalsIgnoreCase(stp1.getTypeName())) {
                Class<? extends ResourceSchema> _class = rs.getClass();

                return new Gson().fromJson(new InputStreamReader(input),_class);
            }
        }

        log.error("failed to deserialize, supported typename(s) are:" + err_msg);
        throw new UnsupportedOperationException("invalid schema definition, cannot find ResourceSchema for the specified typename:" + stp1.typeName);
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
