package org.izdevs.acidium;

import com.mojang.brigadier.CommandDispatcher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

@SpringBootApplication
public class AcidiumApplication {

	public static void main(String[] args) {
		//SpringApplication.run(AcidiumApplication.class, args);

	}


}
