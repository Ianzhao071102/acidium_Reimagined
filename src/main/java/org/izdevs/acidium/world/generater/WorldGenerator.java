package org.izdevs.acidium.world.generater;

import lombok.Getter;
import org.izdevs.acidium.world.World;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class WorldGenerator {

    String name;
    List<Object> options;
    abstract World generate(long seed);
    public WorldGenerator(String name,Object... options){
        this.name = name;
        this.options = new ArrayList<>();
        this.options.addAll(List.of(options));
    }
}
