package org.izdevs.acidium.game.equipment;

import jakarta.annotation.PostConstruct;
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
    protected EquipmentSchema schema;
    @PostConstruct
    public void __set_allow_to_be_crafting_material(){
        if(this.schema.allowToBeCraftingMaterial)
            this.schema.allowedSlots.add(InventoryType._Crafting);
    }
    public Equipment(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
        this.schema.allowedSlots.add(InventoryType.Inventory);
    }
    public Equipment(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage, int maxDurability) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
        this.schema.allowedSlots.add(InventoryType.Inventory);
        this.schema.maxDurability = maxDurability;
    }
    public Equipment(String name) {
        super(name, 100, 1, 0, 0);
    }
}
