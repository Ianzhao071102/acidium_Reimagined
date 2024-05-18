package org.izdevs.acidium.world;

import com.esri.core.geometry.Point;
import lombok.Getter;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.basic.Entity;
import org.jline.nativ.Kernel32;

import java.util.ArrayList;
import java.util.Map;

@Getter
public class TickedWorld {
    public volatile ArrayList<Entity> mobs = new ArrayList<>();
    public volatile ArrayList<Player> players = new ArrayList<Player>();
    Map<Point, Block> map;

    public TickedWorld(Map<Point, Block> map) {
        this.map = map;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addMob(Entity mob) {
        mobs.add(mob);
    }

    public void delPlayer(Player player) {
        players.remove(player);
    }

    public void delMob(Entity mob) {
        mobs.remove(mob);
    }
}
