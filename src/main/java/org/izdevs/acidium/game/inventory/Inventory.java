package org.izdevs.acidium.game.inventory;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.game.equipment.Equipment;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Inventory {
    public final InventoryType type;
    @Setter
    public List<Equipment> items;

    /**
     * here defines the primary_inventory MAX slot sizes
     */
    public Inventory(InventoryType type) {
        this.type = type;
        switch (type) {
            case Inventory -> items = new ArrayList<>(100);
            case Armour -> items = new ArrayList<>(20);
            case Electron -> items = new ArrayList<>(50);
            case _Crafting -> items = new ArrayList<>(10);
        }
    }

    public Inventory(InventoryType type, List<Equipment> items) {
        this.type = type;
        for (int i = 0; i <= items.size() - 1; i++) {
            Equipment _this = items.get(i);
            if (!_this.getSchema().getAllowedSlots().contains(type)) {
                //it is not allowed to be in this type of primary_inventory
                throw new IllegalArgumentException("primary_inventory type not supported:" + _this.getSchema().getAllowedSlots() + "primary_inventory type:" + this.type);
            } else {
                this.items = items;
            }
        }
    }

    public void setItemAt(int id,Equipment item){
        items.set(id,item);
    }
}
