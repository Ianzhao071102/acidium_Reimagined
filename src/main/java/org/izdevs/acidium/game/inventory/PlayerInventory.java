package org.izdevs.acidium.game.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Getter
@NoArgsConstructor
public class PlayerInventory {
    AtomicReference<Inventory> primary = new AtomicReference<>(new Inventory(InventoryType.Inventory));
    AtomicReference<Inventory> crafting = new AtomicReference<>(new Inventory(InventoryType._Crafting));
    AtomicReference<Inventory> armour = new AtomicReference<>(new Inventory(InventoryType.Armour));
    AtomicReference<Inventory> electron = new AtomicReference<>(new Inventory(InventoryType.Electron));

    public AtomicReference<Inventory> getInventoryReference(InventoryType type){
        switch(type){
            case Armour -> {
                return armour;
            }
            case Electron -> {
                return electron;
            }
            case _Crafting -> {
                return crafting;
            }
            case Inventory -> {
                return primary;
            }
        }
        throw new RuntimeException("deprecated _graphics should not be used as a primary_inventory type");
    }
}
