package org.izdevs.acidium.game.equipment;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.inventory.InventoryType;

@Setter
@Getter
public abstract class Equipment{
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
    public Equipment(Entity owner) {
        this.schema.allowedSlots.add(InventoryType.Inventory);
        this.owner = owner;
    }
    public Equipment(int maxDurability,Entity owner) {
        if(maxDurability <= 0){
            throw new IllegalArgumentException("maxDurability cannot be less or equal to 0");
        }
        this.schema.allowedSlots.add(InventoryType.Inventory);
        this.schema.maxDurability = maxDurability;
        this.owner = owner;
    }

    public abstract boolean is_cd_over();
    /**
     * invokes when the equipment runs out of durability
     */
    public abstract void handleBroken();

}
