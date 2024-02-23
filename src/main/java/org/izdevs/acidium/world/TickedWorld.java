package org.izdevs.acidium.world;

import com.esri.core.geometry.Point;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.api.v1.Player;

import java.util.ArrayList;
import java.util.Map;

public class TickedWorld extends World{
    public static ArrayList<Mob> mobs = new ArrayList<>();
    public static ArrayList<Player> players = new ArrayList<Player>();

    public TickedWorld(Map<Point, Block> map) {
        super(map);
    }
    public static void addPlayer(Player player){players.add(player);}
    public static void addMob(Mob mob){mobs.add(mob);}
    public static void delPlayer(Player player){players.remove(player);}
    public static void delMob(Mob mob){mobs.remove(mob);}
}
