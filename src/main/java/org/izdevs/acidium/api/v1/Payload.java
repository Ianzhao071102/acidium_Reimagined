package org.izdevs.acidium.api.v1;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;

public class Payload extends Resource {
    @Getter
    @Setter
    String payload;
    public Payload() {
        super("payload",false);
    }
    public Payload(String payload){
        super("payload",false);
        this.payload = payload;
    }
}
