package org.izdevs.acidium.game.crafting;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class CraftingRecipeHolder {
    @Getter
    static Set<CraftingRecipe> recipes = new HashSet<>();

    public static void registerRecipe(CraftingRecipe recipe){
        recipes.add(recipe);
    }
}
