package org.izdevs.acidium.world;

import lombok.Getter;
import org.izdevs.acidium.api.v1.Structure;

import java.util.ArrayList;
@Getter
public class StructureHolder {
    public static volatile ArrayList<Structure> structures = new ArrayList<>();
    public static void register(Structure structure){
        if(!structures.contains(structure)) structures.add(structure);
        else throw new IllegalArgumentException("the structure is already registered");
    }

}
