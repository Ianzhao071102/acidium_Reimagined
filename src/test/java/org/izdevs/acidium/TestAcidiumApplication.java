package org.izdevs.acidium;

import com.esri.core.geometry.Point;
import org.izdevs.acidium.world.Block;
import org.izdevs.acidium.world.BlockType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration(proxyBeanMethods = false)
public class TestAcidiumApplication {

	@Bean
	@ServiceConnection
	KafkaContainer kafkaContainer() {
		return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
	}

	public static void main(String[] args) {
		//SpringApplication.from(AcidiumApplication::main).with(TestAcidiumApplication.class).run(args);
		Map<Point, Block> description = new HashMap<>();
		description.put(new Point(0,0),new Block(0,0, BlockType.VOID,true));
		System.out.println(description);
    }
}
