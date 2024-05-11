package org.izdevs.acidium.serialization;

import java.io.InputStream;

public interface ResourceDeserializer {
    Resource deserialize(String data);
    Resource deserialize(InputStream input);
}
