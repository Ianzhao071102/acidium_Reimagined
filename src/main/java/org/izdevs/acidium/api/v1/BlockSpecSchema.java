package org.izdevs.acidium.api.v1;


import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.annotations.ResourceSchemaDefinition;
import org.izdevs.acidium.serialization.models.ResourceSchema;

@Getter
@Setter
@ResourceSchemaDefinition("BlockSpec")
//todo make resource schema for every resource type
public class BlockSpecSchema extends ResourceSchema {
    boolean walkable;
}
