package org.izdevs.acidium.api.v1;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.serialization.Resource;
@Getter
@Setter
@Entity
public class BlockSpec extends Resource{
    boolean walkable;
    @Id
    private Long id;

    public BlockSpec(CompoundTag data) {
        this.walkable = data.getString("walkable").equalsIgnoreCase("true");
    }

    public BlockSpec() {

    }
}
