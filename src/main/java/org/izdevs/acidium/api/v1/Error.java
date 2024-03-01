package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.Resource;

public class Error extends Resource {
    public Error() {
        super("Error", false);
    }
    public Error(Throwable cause){
        super(cause.getMessage(), false);
    }
}
