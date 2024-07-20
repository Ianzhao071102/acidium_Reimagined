package org.izdevs.acidium.serialization;

import org.izdevs.acidium.serialization.models.ResourceSchema;

import java.io.InputStream;

public abstract class ResourceDeserializer {
    DeserializerTypes type;
    public ResourceDeserializer(DeserializerTypes type){
        this.type = type;
    }
    public abstract Resource deserialize(String data);
    public abstract Resource deserialize(InputStream input);
}
