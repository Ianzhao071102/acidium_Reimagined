package org.izdevs.acidium.game.equipment;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.world.World;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class Equipment extends Entity {
    int maxDurability;
    CraftingRecipe recipe;
    /**
     * slots that is allowed to be placed on
     * defaults to: primary_inventory
     */
    Set<InventoryType> allowedSlots = new HashSet<>();

    public Equipment(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage,CraftingRecipe recipe) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
        allowedSlots.add(InventoryType.Inventory);
        this.recipe = recipe;
    }
    public Equipment(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage,CraftingRecipe recipe, int maxDurability) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
        allowedSlots.add(InventoryType.Inventory);
        this.recipe = recipe;
        this.maxDurability = maxDurability;
    }
    public Equipment(String name) {
        super(name, 100, 1, 0, 0);
        this.recipe = new CraftingRecipe();
    }
}
