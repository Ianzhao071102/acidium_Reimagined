package org.izdevs.acidium.world;

import org.izdevs.acidium.api.v1.BlockSpec;

import java.util.ArrayList;

public class BlockDataHolder {
    public static ArrayList<BlockSpec> registered = new ArrayList<>();
    public static void registerSpec(BlockSpec spec){
        registered.add(spec);
    }
}
