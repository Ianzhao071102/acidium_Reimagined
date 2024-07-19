package org.izdevs.acidium.game.entity;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.world.World;
import org.springframework.context.event.EventListener;

public class ItemEntity extends Entity {
    public ItemEntity(World world) {
        super(world, "Item", 0, 1, 2, 0);
    }

}
