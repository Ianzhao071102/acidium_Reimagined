package org.izdevs.acidium;

import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.tick.TickManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.izdevs.acidium.serialization.YamlParser.registerYamlDef;

@Configuration
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableScheduling
public class AcidiumApplication{
	static Logger logger = LoggerFactory.getLogger(AcidiumApplication.class);
	static ArrayList<Resource> resources = new ArrayList<>();
	public static void main(String[] args) throws NoSuchMethodException, IOException {
		SpringApplication.run(AcidiumApplication.class, args);
		TickManager.init();
		loadYaml();
	}
	public static void loadYaml() throws IOException {
		org.springframework.core.io.Resource[] resource = getXMLResources();
		if(resource.length == 0){
			logger.debug("no yaml found on classpath");
			return;
		}
		for(int i=0;i<= resource.length;i++){
			org.springframework.core.io.Resource resource1 = resource[i];
			URL path = resource1.getURL();
			try(InputStream stream = new FileInputStream(String.valueOf(path))){
				registerYamlDef(stream);
			}
		}
	}
	private static org.springframework.core.io.Resource[] getXMLResources() throws IOException
	{
		ClassLoader classLoader = MethodHandles.lookup().getClass().getClassLoader();
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);

		return resolver.getResources("classpath:*.yaml");

	}
}
