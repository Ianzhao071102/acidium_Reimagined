package org.izdevs.acidium.world;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
public class Block{

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
    @Override
    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
