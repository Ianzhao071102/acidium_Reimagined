package org.izdevs.acidium.serialization;

import org.izdevs.acidium.serialization.models.ResourceSchema;

import java.io.InputStream;

public interface ResourceDeserializer {
    Resource deserialize(String data);
    Resource deserialize(InputStream input);
}
