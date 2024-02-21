package org.izdevs.acidium.serialization;

import java.util.HashMap;
import java.util.Map;

public interface AbstractResource {
    String name = "";
    Map<String,Resource> resource = new HashMap<>();
    default String getName() {
        return name;
    }
    void setResource();
    void setResourceByKey();
    void getKeys();
    void getValueByKey();
}
