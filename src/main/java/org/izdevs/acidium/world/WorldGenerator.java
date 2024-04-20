package org.izdevs.acidium.world;

public interface WorldGenerator {
    String seed = "";
    String type = "";

    World generate(long seed);
}
