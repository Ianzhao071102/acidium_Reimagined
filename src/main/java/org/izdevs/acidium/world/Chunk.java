package org.izdevs.acidium.world;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Chunk {
    Block[][] blocks = new Block[16][16]; //a chunk is 16 by 16 blocks
    public void setBlockAt(Block dest,Location location){
        int x = location.getX();
        int y = location.getY();
        if(x > 15 || y > 15){
            throw new IllegalArgumentException("index out of bounds,a chunk is 16 by 16, setting blocks in a chunk is relative to 0,0 in the chunk, not world");
        }
        blocks[x][y] = dest;
    }
}
