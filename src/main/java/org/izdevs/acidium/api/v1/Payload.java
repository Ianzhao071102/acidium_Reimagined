package org.izdevs.acidium.api.v1;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;

@Setter
@Getter
public class Payload {
    String payload;
    public Payload(String payload){
        this.payload = payload;
    }


}
