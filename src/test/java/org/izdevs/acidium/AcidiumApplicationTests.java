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
  //worlds do not generate
    @TestBean
    public static boolean generateWorld() {
      return falae;
    }
    @Test
    public void contextLoads() {
      Logger logger = LoggerFactory.getLogger(this.getClass());

      logger.warn("world generation is skipped due to explicit test env");
    }
    @Test
    public void taskTest(){
      DelayedTask task = new DelayedTask(() -> {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        logger.info("logger test of tasking has been sucessful");
      },1);
      LoopManager.scheduleAsyncDelayedTask(task);
    }
  
}
