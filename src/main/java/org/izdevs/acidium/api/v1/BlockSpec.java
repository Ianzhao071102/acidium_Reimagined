package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.serialization.Resource;
@Getter
@Setter
public class BlockSpec extends Resource {
    boolean walkable;
    public BlockSpec(CompoundTag data) {
        this.walkable = data.getString("walkable").equalsIgnoreCase("true");
    }
}
