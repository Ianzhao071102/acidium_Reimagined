package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.annotations.ResourceSchemaDefinition;

@ResourceSchemaDefinition("Error")
public class ErrorSchema {
    Throwable cause;
    String message;
}
