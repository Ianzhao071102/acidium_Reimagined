package org.izdevs.acidium.world;

import com.esri.core.geometry.Point;
import org.izdevs.acidium.basic.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldData {
    public Map<Point,Block> map = new HashMap<>();
    public Set<Entity> entities = new HashSet<>();
}
