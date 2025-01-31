package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.annotations.ResourceSchemaDefinition;
import org.springframework.stereotype.Component;

@ResourceSchemaDefinition("Error")
@Component
public class ErrorSchema {
    Throwable cause;
    String message;
}
