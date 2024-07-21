package org.izdevs.acidium.serialization.parsers;

import com.badlogic.gdx.Input;
import com.google.gson.Gson;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.izdevs.acidium.serialization.DeserializerTypes;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@Component
public class JSONParser extends ResourceDeserializer {
    @Override
    public Resource deserialize(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data,Resource.class);
    }

    @Override
    public Resource deserialize(InputStream input) {
        InputStreamReader reader = new InputStreamReader(input);
        return new Gson().fromJson(reader,Resource.class);
    }

    @Override
    public <T> Resource deserialize(InputStream input, T type) {
        InputStreamReader reader = new InputStreamReader(input);
        return (Resource) new Gson().fromJson(reader,type.getClass());
    }

    public JSONParser(){
        super(DeserializerTypes.JSON);
    }
}
