package org.izdevs.acidium.world;

import lombok.Getter;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.basic.Entity;

import java.util.ArrayList;

@Getter
public class TickedWorld {
    public volatile ArrayList<Entity> mobs = new ArrayList<>();
    public volatile ArrayList<Player> players = new ArrayList<Player>();
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
