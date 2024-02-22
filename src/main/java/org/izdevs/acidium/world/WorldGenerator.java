package org.izdevs.acidium.world;

public interface WorldGenerator {
    World generate(String seed);
    String seed = "";
    String type = "";
}
