package org.izdevs.acidium.game.crafting;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.equipment.EquipmentHolder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class CraftingRecipeHolder {
    @Getter
    static Set<CraftingRecipe> recipes = new HashSet<>();

    public static void registerRecipe(CraftingRecipe recipe){
        recipes.add(recipe);
    }
    @PostConstruct
    public static void init() throws InstantiationException, IllegalAccessException {
        Logger logger = LoggerFactory.getLogger(CraftingRecipeHolder.class);
        Reflections reflections = new Reflections("org.izdevs.acidium.game");

        Set<Class<?>> subTypes =
                reflections.get(SubTypes.of(CraftingRecipe.class).asClass());
        for (Class<?> subType : subTypes) {
            registerRecipe((CraftingRecipe) subType.newInstance());
            logger.debug(subType.newInstance().toString());
        }
    }


}
