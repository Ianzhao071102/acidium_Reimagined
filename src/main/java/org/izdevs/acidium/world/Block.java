package org.izdevs.acidium.world;

import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;

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

}
