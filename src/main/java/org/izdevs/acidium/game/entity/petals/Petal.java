package org.izdevs.acidium.game.entity.petals;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.world.World;

public class Petal extends Entity {
    protected Petal(World world, double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(world, "__Petal", movementSpeed, health, hitboxRadius, bDamage);
    }
}
