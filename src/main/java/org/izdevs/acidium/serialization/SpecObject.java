package org.izdevs.acidium.serialization;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SpecObject {
    String key;
    Object value;
    public SpecObject(String key,Object value){
        this.key = key;
        this.value = value;
    }
}
