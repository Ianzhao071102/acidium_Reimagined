package org.izdevs.acidium.game.equipment;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.world.World;
import org.springframework.stereotype.Component;

@Component
public class Electron extends Equipment {

    /**
     * an electron has health of 20
     * movement speed of 0.2
     * hitbox radius of 1
     * body_damage of 5
     */
    public Electron(World world, Entity equip) {
        super(world, "electron", 0.2, 20, 1, 5, equip, new CraftingRecipe());
        this.allowedSlots.add(InventoryType.Electron);

        //un-craftable
        this.recipe = new CraftingRecipe();
    }

    public Electron() {
        super("electron");
    }
}
