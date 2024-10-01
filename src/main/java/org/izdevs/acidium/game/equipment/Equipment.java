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
public abstract class Equipment extends Entity {
    //reserved cd to avoid shits from happening
    public static final int reserved_cd = Integer.MAX_VALUE;

    boolean isBroken = false;

    protected EquipmentSchema schema;
    Entity owner;
    @PostConstruct
    public void __set_allow_to_be_crafting_material(){
        if(this.schema.allowToBeCraftingMaterial)
            this.schema.allowedSlots.add(InventoryType._Crafting);
    }
    public Equipment(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage,Entity owner) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
        this.schema.allowedSlots.add(InventoryType.Inventory);
        this.owner = owner;
    }
    public Equipment(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage, int maxDurability,Entity owner) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
        if(maxDurability <= 0){
            throw new IllegalArgumentException("maxDurability cannot be less or equal to 0");
        }
        this.schema.allowedSlots.add(InventoryType.Inventory);
        this.schema.maxDurability = maxDurability;
        this.owner = owner;
    }
    public Equipment(String name) {
        super(name, 100, 1, 0, 0);
    }

    public abstract boolean is_cd_over();
    public abstract void use();

    /**
     * invokes when the equipment runs out of durability
     */
    public abstract void handleBroken();

}
