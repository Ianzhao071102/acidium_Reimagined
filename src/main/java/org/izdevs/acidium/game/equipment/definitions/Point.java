package org.izdevs.acidium.game.equipment.definitions;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.utils.RandomUtils;
import org.izdevs.acidium.world.World;

public class Point extends Entity {
    public Point(World world) {
        super(world, "POINT-" + RandomUtils.getRandomString(5), 1, 400, 2, 50);
    }

    @Override
    public void handleHitboxCollision(Entity another) {
        another.setHealth(another.getHealth() - this.getHealth());
        this.setHealth(this.getHealth() - another.getBDamage());
    }

    @Override
    public void handleDeath() {
        super.handleDeath();
    }
}
