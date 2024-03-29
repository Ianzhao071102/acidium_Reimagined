package org.izdevs.acidium.game.equipment;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.world.World;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

public class Equipment extends Entity {
    @Getter
    @Setter
    CraftingRecipe recipe;
    @Id
    int id;
    @Getter
    Entity equipEntity;

    /**
     * slots that is allowed to be placed on
     * defaults to: inventory
     */
    @Getter
    @Setter
    Set<InventoryType> allowedSlots = new HashSet<>();

    public Equipment(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage, Entity equip, CraftingRecipe recipe) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
        EquipmentHolder.register(this);
        this.equipEntity = equip;

        allowedSlots.add(InventoryType.Inventory);
        this.recipe = recipe;
    }

    /**
     * equip current equipment to its owner's electron slot
     */
    public void equipElectronSlot(int slotId) {
        if (this.equipEntity.getInventory().getItems().contains(this)) {
            this.equipEntity.getInventory().getItems().remove(this);
        } else {
            throw new IllegalArgumentException("the item to be equipped is not in inventory...");
        }
        this.equipEntity.getElectronInv().getItems().add(this);

    }

    /**
     * equip current equipment to its owner's armor slot
     */
    public void equipArmorSlot(int slotId) {
        if (this.equipEntity.getInventory().getItems().contains(this)) {
            this.equipEntity.getInventory().getItems().remove(this);
        } else {
            throw new IllegalArgumentException("the item to be equipped is not in inventory...");
        }
        this.equipEntity.getArmourInv().getItems().add(this);

    }

    public Equipment(String name) {
        super(name, 100, 1, 0, 0);

        //defaults to un-craftable
        this.recipe = new CraftingRecipe();
    }
}
