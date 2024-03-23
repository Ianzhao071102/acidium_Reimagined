package org.izdevs.acidium.game.inventory;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.game.equipment.Equipment;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Inventory {
    InventoryType type;
    @Setter
    List<Equipment> items = new ArrayList<>();

    public Inventory(InventoryType type) {
        this.type = type;
    }

    public Inventory(InventoryType type, List<Equipment> items) {
        this.type = type;
        for (int i = 0; i <= items.size() - 1; i++) {
            Equipment _this = items.get(i);
            if (!_this.getAllowedSlots().contains(type)) {
                //it is not allowed to be in this type of inventory
                throw new IllegalArgumentException("inventory type not supported:" + _this.getAllowedSlots() + "inventory type:" + this.type);
            } else {
                this.items = items;
            }
        }
    }

}
