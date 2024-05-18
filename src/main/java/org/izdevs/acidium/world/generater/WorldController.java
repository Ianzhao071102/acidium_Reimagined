package org.izdevs.acidium.world.generater;

import org.izdevs.acidium.game.entity.spawner.DefaultSpawner;
import org.izdevs.acidium.world.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Component
public class WorldController {
    @Autowired
    WorldGenerator generator;

    public static ArrayList<World> worlds = new ArrayList<>();

    public void generateWorld(long seed) {
        //still uses default world generator just for now...
        World world = generator.generate(seed);
        world.setSpawner(new DefaultSpawner());
        worlds.add(world);
    }
}
