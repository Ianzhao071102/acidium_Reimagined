package org.izdevs.acidium.world;

import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;

import java.util.Arrays;

@Getter
@Setter
public class Block {
    @Getter
    @Setter
    CompoundTag tag;
    int x;
    int y;
    BlockType type;
    boolean walkable;
    public Block(int x,int y,BlockType type,boolean walkable){
        this.x = x;
        this.y = y;
        this.type = type;
        this.walkable = walkable;
    }

    /**
     * @return a string representation of the object.
     * @apiNote serializes itself
     */
    @Override
    public String toString() {
        String[] data = new String[4];
        data[0] = String.valueOf(x);
        data[1] = String.valueOf(y);
        data[2] = type.name();
        data[3] = String.valueOf(walkable);
        return Arrays.toString(data);
    }
}
