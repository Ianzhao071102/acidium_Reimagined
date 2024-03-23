package org.izdevs.acidium.game.crafting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CraftingRecipe {
    boolean craftable = true;
    Set<CraftingSlot> slots = new HashSet<>();

    public CraftingRecipe(CraftingSlot... craftingSlots) {
        Collections.addAll(slots, craftingSlots);
    }

    public boolean validate(CraftingRecipe input) {
        if (!craftable) return false;
        else {
            return slots.equals(input.slots);
        }

    }

    /**
     * constructors for un-craftable items
     */
    public CraftingRecipe() {
        this.craftable = false;
    }
}
