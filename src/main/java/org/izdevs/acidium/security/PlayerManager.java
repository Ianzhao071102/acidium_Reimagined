package org.izdevs.acidium.security;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.api.v1.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayerManager {
    @Getter
    @Setter
    static int playerCount = 0;
    public static Set<Player> players = new HashSet<>();
    public static void add(Player player){
        players.add(player);
        playerCount = players.size();
    }
    public static void remove(Player player){
        players.remove(player);
    }

}
