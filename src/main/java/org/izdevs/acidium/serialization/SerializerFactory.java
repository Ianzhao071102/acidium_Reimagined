package org.izdevs.acidium.serialization;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SerializerFactory {
    @Autowired
    Set<ResourceDeserializer> deserializers;
    public ResourceDeserializer getDeserializer(DeserializerTypes type){
        for(int i=0;i<=deserializers.size()-1;i++){
            ResourceDeserializer _this = deserializers.iterator().next();
            if(_this.type.equals(type)){
                return _this;
            }
        }
        throw new IllegalArgumentException("specified type: " + type.name() + " of deserializer is NOT found globally.");
    }


}
