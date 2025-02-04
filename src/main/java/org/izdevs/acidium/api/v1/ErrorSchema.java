package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.annotations.ResourceSchemaDefinition;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.stereotype.Component;

@ResourceSchemaDefinition("Error")
@Component
public class ErrorSchema extends ResourceSchema {
    Throwable cause;
    String message;
}
