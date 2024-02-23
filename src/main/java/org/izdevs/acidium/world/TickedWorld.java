package org.izdevs.acidium.world;

import com.esri.core.geometry.Point;
import org.izdevs.acidium.api.v1.Mob;


import java.util.ArrayList;
import java.util.Map;

public class TickedWorld extends World{
    ArrayList<Mob> mobs = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    public TickedWorld(Map<Point, Block> map) {
        super(map);
    }
}
