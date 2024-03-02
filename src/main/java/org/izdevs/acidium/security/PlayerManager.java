package org.izdevs.acidium.security;

import org.izdevs.acidium.api.v1.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayerManager {
    public static Set<Player> players = new HashSet<>();
    public static void add(Player player){
        players.add(player);
    }
    public static void remove(Player player){
        players.remove(player);
    }
}
