package org.izdevs.acidium.game.equipment.entity;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.equipment.EquipmentSchema;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.world.World;
import org.springframework.stereotype.Component;

@Component
public class Neutron extends Equipment {
    /**
     * defensive equipment that cannot be put onto electron slot
     * health is 100
     * hitbox radius is 1
     */
    public Neutron(World world, Entity equip) {
        super(world, "neutron", 0, 100, 1, 2);
        this.schema.allowedSlots.add(InventoryType.Armour);
    }

    public Neutron() {
        super("neutron");
    }
}
