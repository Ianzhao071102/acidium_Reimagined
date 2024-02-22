package org.izdevs.acidium.world;

import lombok.Getter;
import lombok.Setter;

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
