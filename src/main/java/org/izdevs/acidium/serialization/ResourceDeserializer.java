package org.izdevs.acidium.serialization;

import org.izdevs.acidium.serialization.models.ResourceSchema;

import java.io.InputStream;

public abstract class ResourceDeserializer {
    DeserializerTypes type;
    public ResourceDeserializer(DeserializerTypes type){
        this.type = type;
    }
    public abstract ResourceSchema deserialize(String data);
    public abstract ResourceSchema deserialize(InputStream input);
    public abstract <T> ResourceSchema deserialize(InputStream input , T type);
}
