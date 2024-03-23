package org.izdevs.acidium.game;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.world.World;

public class Neutron extends Equipment{
    /**
     * defensive equipment that cannot be put onto electron slot
     */
    public Neutron(World world,Entity equip) {
        super(world, "neutron", 0, 100, 1, 2, equip);
        this.allowedSlots.add(Slot.ARMOUR);
    }
}
