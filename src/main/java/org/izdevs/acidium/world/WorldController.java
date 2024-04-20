package org.izdevs.acidium.world;

import org.izdevs.acidium.api.v1.DefaultSpawner;

import java.util.ArrayList;

public class WorldController {
    public static ArrayList<World> worlds = new ArrayList<>();

    public static void generateWorld(long seed) {
        //still uses default world generator just for now...
        DefaultWorldGenerator gen = new DefaultWorldGenerator();
        World world = gen.generate(seed);
        world.setSpawner(new DefaultSpawner());
        worlds.add(world);
    }
}
