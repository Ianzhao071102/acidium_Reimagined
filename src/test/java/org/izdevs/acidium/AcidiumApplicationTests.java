package org.izdevs.acidium;

import com.google.gson.Gson;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.crafting.CraftingSlot;
import org.izdevs.acidium.game.equipment.Equipment;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AcidiumApplicationTests {
    @Test
    public void gson(){
        Logger logger = LoggerFactory.getLogger(this.getClass());

        //TODO MAKE POSITION'S NBT DATA(equipment)
        logger.info(new Gson().toJson(new CraftingRecipe(new CraftingSlot(0,0,new Equipment("electron")),new CraftingSlot(0,1,new Equipment("positron")))));
    }
}
