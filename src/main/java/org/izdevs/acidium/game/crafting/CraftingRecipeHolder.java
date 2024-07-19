package org.izdevs.acidium.game.crafting;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CraftingRecipeHolder {
    @Autowired
    ResourceFacade facade;
    @Getter
    static Set<CraftingRecipe> recipes = new HashSet<>();

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        Map<String, CraftingRecipe> map = facade.getResourceBeans(CraftingRecipe.class);
        if(map.isEmpty()) {}
        else{
            for(int i=0;i<=map.keySet().size()-1;i++){
                String name = map.keySet().iterator().next();
                CraftingRecipe recipe = map.get(name);
                assert recipe != null;

                recipes.add(recipe);
            }
        }
    }
}
