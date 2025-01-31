package org.izdevs.acidium.api.v1;

import lombok.Getter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.inventory.PlayerInventory;

import java.util.UUID;

@Getter
public class Player extends Entity {
    public volatile PlayerInventory inventory;
    UUID uuid;
    User user;

    public Player(User user,double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(user.username, movementSpeed, health, hitboxRadius, bDamage);
        this.user = user;
        this.inventory = new PlayerInventory(this.user);
    }
}
