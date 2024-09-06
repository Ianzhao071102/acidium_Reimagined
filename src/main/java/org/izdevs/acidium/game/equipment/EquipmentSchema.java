package org.izdevs.acidium.game.equipment;

import lombok.AllArgsConstructor;
import org.izdevs.acidium.game.inventory.InventoryType;

import java.util.Set;

@AllArgsConstructor
public class EquipmentSchema {
    public int maxDurability;
    public Set<InventoryType> allowedSlots;
    public String name;
    public boolean allowToBeCraftingMaterial = true;
}
