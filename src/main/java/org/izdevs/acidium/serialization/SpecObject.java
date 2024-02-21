package org.izdevs.acidium.serialization;

public class SpecObject {
    String key;
    Object value;

    public void setValue(Object value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }
}
