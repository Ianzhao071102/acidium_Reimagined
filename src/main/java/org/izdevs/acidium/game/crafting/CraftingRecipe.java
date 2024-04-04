package org.izdevs.acidium.game.crafting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
public class CraftingRecipe {
    String name;
    boolean ordered = false;
    boolean craftable = true;
    Set<CraftingSlot> slots = new HashSet<>();

    public CraftingRecipe(CraftingSlot... craftingSlots) {
        Collections.addAll(slots, craftingSlots);
    }
    public CraftingRecipe(String name){
        this.name = name;
    }
    public boolean validate(CraftingRecipe input) {
        if (!craftable) return false;
        else {
            return slots.equals(input.slots);
        }

    }

    /**
     * constructors for un-craft-able items
     */
    public CraftingRecipe() {
        this.craftable = false;
    }
}
