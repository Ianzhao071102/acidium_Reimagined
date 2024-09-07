package org.izdevs.acidium.game.equipment.entity;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.equipment.EquipmentSchema;
import org.izdevs.acidium.game.equipment.schemas.ElectronSchema;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.world.World;
import org.springframework.stereotype.Component;


public class Electron extends Equipment {

    /**
     * an electron has health of 20
     * movement speed of 0.2
     * hitbox radius of 1
     * body_damage of 5
     */
    long owner_entity_id = -1;
    public Electron(World world) {
        super(world, "electron", 0.2, 20, 1, 5);
        this.schema = new ElectronSchema();
        this.schema.allowedSlots.add(InventoryType.Electron);
    }

    public Electron() {
        super("electron");
    }
}
