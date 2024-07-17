package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.Resource;
import org.springframework.stereotype.Component;

@Component
public class Error extends Resource {
    Throwable cause;
    public Error() {}
    public Error(Throwable cause){
        this.cause = cause;
    }
}
