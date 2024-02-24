package org.izdevs.acidium.api.v1;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.serialization.Resource;

@Getter
@Setter
public class BlockSpec extends Resource {
    @Id
    @GeneratedValue
    int id;
    boolean walkable;
    public BlockSpec(CompoundTag data) {
        super(data.getString("name"), false);
        this.data = data;
        this.walkable = data.getString("walkable").equalsIgnoreCase("true");
    }
    public BlockSpec(){
        super("unset",false);
    }
}
