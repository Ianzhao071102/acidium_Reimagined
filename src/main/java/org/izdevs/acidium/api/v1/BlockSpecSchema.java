package org.izdevs.acidium.api.v1;


import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.annotations.ResourceSchemaDefinition;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ResourceSchemaDefinition("BlockSpec")
@Component
public class BlockSpecSchema extends ResourceSchema {
    boolean walkable;
}
