package org.izdevs.acidium.serialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SerializerFactory {
    @Qualifier("JSONParser")
    @Autowired
    ResourceDeserializer jsonParser;

    @Qualifier("NBTParser")
    @Autowired
    ResourceDeserializer nbtParser;

    public ResourceDeserializer getDeserializer(DeserializerTypes types){
        switch(types){
            case NBT -> {
                return nbtParser;
            }
            case JSON -> {
                return jsonParser;
            }
        }
        return null;
    }


}
