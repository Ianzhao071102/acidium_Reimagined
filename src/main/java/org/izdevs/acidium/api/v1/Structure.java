package org.izdevs.acidium.api.v1;

import com.esri.core.geometry.Point;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.Block;
import org.izdevs.acidium.world.generater.StructureHolder;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Structure extends Resource {
    public Structure(String name) {
    }
    Map<Point, Block> description = new HashMap<>();
}
