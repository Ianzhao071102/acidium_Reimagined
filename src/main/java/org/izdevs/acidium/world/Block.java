package org.izdevs.acidium.world;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.api.v1.BlockType;

@Getter
@Setter
public class Block {
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
