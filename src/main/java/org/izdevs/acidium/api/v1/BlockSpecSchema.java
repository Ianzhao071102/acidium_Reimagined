package org.izdevs.acidium.api.v1;


import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class BlockSpecSchema extends ResourceSchema {
    boolean walkable;
}
