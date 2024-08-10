package org.izdevs.acidium.game.crafting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.game.equipment.Equipment;

@Getter
@Setter
@AllArgsConstructor
/**
 * @see org.izdevs.acidium.game.inventory.PlayerInventory
 * @since 0.2.1
 * Not yet implemented with PlayerInventory
 */
public class ItemStack {
    int stackCount;
    Equipment item;
    int durability;
}