package org.izdevs.acidium.api.v1;

import lombok.Getter;
import org.izdevs.acidium.game.inventory.PlayerInventory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Player{

    public volatile PlayerInventory inventory;
    UUID uuid;
    User user;

    public Player(User user) {
        this.user = user;
    }

    public Set<String> parties_joined = new HashSet<>();
}
