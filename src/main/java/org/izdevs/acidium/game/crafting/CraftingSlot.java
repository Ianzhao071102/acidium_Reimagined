package org.izdevs.acidium.game.crafting;

import lombok.Getter;
import org.izdevs.acidium.game.equipment.Equipment;

@Getter
public class CraftingSlot {
    Equipment content;
    int x, y;
    boolean disordered = false;
    public CraftingSlot(int x, int y, Equipment equipment) {
        this.x = x;
        this.y = y;
        this.content = equipment;
    }

    public CraftingSlot(Equipment equipment){
        this.content = equipment;
        this.disordered = true;
    }
}
