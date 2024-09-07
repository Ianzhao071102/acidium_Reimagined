package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.annotations.ResourceSchemaDefinition;

@ResourceSchemaDefinition
public class ErrorSchema {
    Throwable cause;
    String message;
}
