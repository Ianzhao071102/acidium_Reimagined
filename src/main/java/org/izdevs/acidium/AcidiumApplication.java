package org.izdevs.acidium;

import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.izdevs.acidium.tick.TickManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;

@Configuration
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableScheduling
public class AcidiumApplication{
	static ArrayList<Resource> resources = new ArrayList<>();
	public static void main(String[] args) throws NoSuchMethodException {
		SpringApplication.run(AcidiumApplication.class, args);
		TickManager.init();
		resources = ResourceFacade.getResources();
	}
}
