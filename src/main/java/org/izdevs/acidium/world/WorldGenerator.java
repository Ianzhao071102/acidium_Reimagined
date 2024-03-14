package org.izdevs.acidium.world;

public interface WorldGenerator {
    World generate(long seed);
    String seed = "";
    String type = "";
}
