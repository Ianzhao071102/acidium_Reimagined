package org.izdevs.acidium.world.generater;

import org.izdevs.acidium.world.World;

public interface WorldGenerator {
    String seed = "";
    String type = "";

    World generate(long seed);
}
