package org.izdevs.acidium.serialization.parsers;

import com.google.gson.Gson;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Component
public class JSONParser implements ResourceDeserializer {

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
