package org.izdevs.acidium.game;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.world.World;

import java.util.HashSet;
import java.util.Set;

public class Equipment extends Entity {
    @Getter
    Entity equipEntity;
    /**
     * slots that is allowed to be placed on
     * defaults to: inventory
     */
    @Getter
    @Setter
    Set<Slot> allowedSlots = new HashSet<>();

    public Equipment(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage,Entity equip) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
        EquipmentHolder.register(this);
        this.equipEntity = equip;

        allowedSlots.add(Slot.INVENTORY);
    }
}
