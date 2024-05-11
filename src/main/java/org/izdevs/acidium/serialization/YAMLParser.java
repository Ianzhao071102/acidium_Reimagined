package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class YAMLParser implements ResourceDeserializer{

    @Override
    public Resource deserialize(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data,Resource.class);
    }

    @Deprecated
    @Override
    public Resource deserialize(InputStream input) {
        throw new UnsupportedOperationException("calling YAML/JSON parser with input stream is NOT supported");
    }
}
