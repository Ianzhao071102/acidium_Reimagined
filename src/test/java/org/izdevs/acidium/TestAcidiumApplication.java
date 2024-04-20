package org.izdevs.acidium;

import com.google.gson.Gson;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.crafting.CraftingSlot;
import org.izdevs.acidium.game.equipment.Equipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestAcidiumApplication {


	public static void main(String[] args) {
		//TODO MAKE POSITION'S NBT DATA(equipment)
		SpringApplication.from(AcidiumApplication::main).with(TestAcidiumApplication.class).run(args);
	}
}
