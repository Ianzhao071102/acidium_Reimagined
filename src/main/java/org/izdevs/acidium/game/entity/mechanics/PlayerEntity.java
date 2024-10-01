package org.izdevs.acidium.game.entity.mechanics;

import lombok.Getter;
import org.izdevs.acidium.world.World;

public class PlayerEntity extends LivingEntity{
    @Getter
    String username;
    public PlayerEntity(World world, String username, double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(world, "PLAYER-"+username, movementSpeed, health, hitboxRadius, bDamage);
        this.username = username;
    }
}
