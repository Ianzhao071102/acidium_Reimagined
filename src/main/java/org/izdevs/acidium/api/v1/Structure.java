package org.izdevs.acidium.api.v1;

import com.esri.core.geometry.Point;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.Block;
import org.izdevs.acidium.world.StructureHolder;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Structure extends Resource {
    //TODO FINISH SOME STRUCTURES IN THIS FORM AND ALSO SERIALIZATION TO AND FROM NBT
    public Structure(String name) {
        super(name, false);
    }
    Map<Point, Block> description = new HashMap<>();
    public void registerToHolder(){
        StructureHolder.register(this);
    }
}
