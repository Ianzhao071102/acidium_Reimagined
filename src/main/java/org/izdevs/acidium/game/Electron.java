package org.izdevs.acidium.game;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.world.World;

public class Electron extends Equipment {
    /**
     * an electron has health of 20
     */
    int health = 20;

    public Electron(World world, Entity equip) {
        super(world, "electron", 0.2, 2, 1, 5,equip);
        this.allowedSlots.add(Slot.ELECTRON);
    }
}
