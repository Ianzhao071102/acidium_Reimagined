package org.izdevs.acidium;

import com.google.gson.Gson;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.crafting.CraftingSlot;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


@SpringBootTest
class AcidiumApplicationTests {
  //worlds do not generate
    @Bean(name = "generateWorld")
    @Primary
    public static boolean generateWorld() {
      return false;
    }
    @Test
    public void contextLoads() {
      Logger logger = LoggerFactory.getLogger(this.getClass());

      logger.warn("world generation is skipped due to explicit test env");
    }
  
}
