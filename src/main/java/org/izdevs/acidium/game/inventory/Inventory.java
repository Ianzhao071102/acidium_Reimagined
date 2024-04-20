package org.izdevs.acidium.game.inventory;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Inventory {
    InventoryType type;
    @Setter
    List<Equipment> items;


    /**
     * here defines the inventory MAX slot sizes
     */
    public Inventory(InventoryType type) {
        this.type = type;
        switch (type) {
            case Inventory -> {
                items = new ArrayList<>(100);
            }
            case Armour -> {
                items = new ArrayList<>(20);
            }
            case Electron -> {
                items = new ArrayList<>(50);
            }
            case _Crafting -> {
                items = new ArrayList<>(10);
            }
        }
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

    /**
     * WELL HERE IS THE INVENTORY SLOT ID SCHEMA:
     * 0-100 INVENTORY (PERSISTS FOREVER AND HAS 100 SLOTS)
     * 101-121 ARMOUR SLOTS
     * 122-172 ELECTRON SLOTS
     * 173-183 CRAFTING SLOTS THAT HAS ONLY 9 SLOTS WITH A CRAFTING SLOT
     */
    public static InventoryType getTypeBySlotId(int id) {
        if (NumberUtils.isInRange(9, 100, id)) return InventoryType.Inventory;
        else if (NumberUtils.isInRange(101, 121, id)) return InventoryType.Armour;
        else if (NumberUtils.isInRange(122, 172, id)) return InventoryType.Electron;
        else if (NumberUtils.isInRange(173, 183, id)) return InventoryType._Crafting;
        else throw new IllegalArgumentException("inventory id is OUT OF BOUNDS, MAX VALUE IS 182, GOT INSTEAD:" + id);
    }

    public static Inventory getInventoryOfPlayerByType(InventoryType type, Player player) {
        Inventory op_inv = null;
        switch (type) {
            case Inventory -> {
                op_inv = player.getInventory();
            }
            case _Crafting -> {
                op_inv = player.getCraftingGrid();
            }
            case Armour -> {
                op_inv = player.getArmourInv();
            }
            case Electron -> {
                op_inv = player.getElectronInv();
            }
            default -> {
                throw new IllegalArgumentException("deprecated inventory type of illegal argument is passed or memory fucks leaks");
            }
        }
        return op_inv;
    }

    public static int getInnerInventorySlotId(int id) {
        //pray to god if this works
        if (NumberUtils.isInRange(9, 100, id)) return id - 9;
        else if (NumberUtils.isInRange(101, 121, id)) return id - 101;
        else if (NumberUtils.isInRange(122, 172, id)) return id - 122;
        else if (NumberUtils.isInRange(173, 183, id)) return id - 173;
        else
            throw new IllegalArgumentException("inventory id is OUT OF BOUNDS, MAX VALUE IS 182, GOT INSTEAD:" + id);
    }

}
