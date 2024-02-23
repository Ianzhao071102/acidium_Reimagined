package org.izdevs.acidium;

import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.izdevs.acidium.tick.TickManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.izdevs.acidium.serialization.NBTParser.registerNBTDef;

@Configuration
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableScheduling
public class AcidiumApplication{
	static Logger logger = LoggerFactory.getLogger(AcidiumApplication.class);
	static ArrayList<Resource> resources = new ArrayList<>();
	public static void main(String[] args) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException {
		SpringApplication.run(AcidiumApplication.class, args);
		TickManager.init();
		loadNBT();
		logger.info("starting resource facade, registering....");
		ResourceFacade.start();
	}
	public static void loadNBT() throws IOException {
		org.springframework.core.io.Resource[] resource = getXMLResources();
		if(resource.length == 0){
			logger.info(resource.length + " resources was/were found");
			logger.debug("no yaml found on classpath");
			return;
		}
		for(int i=0;i<= resource.length-1;i++){
			org.springframework.core.io.Resource resource1 = resource[i];;
			try(InputStream stream = new FileInputStream(resource1.getFile())){
				registerNBTDef(stream);
				logger.info("found NBT resource file... : " + resource1);
			}
		}
	}
	private static org.springframework.core.io.Resource[] getXMLResources() throws IOException
	{
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		try {
			org.springframework.core.io.Resource[] metaInfResources = resourcePatternResolver
					.getResources("classpath*:*.nbt");
			for(org.springframework.core.io.Resource r : metaInfResources){
				logger.debug(r.getURI() + "found resource nbt");
			}
			return metaInfResources;
		}catch(Throwable e){
			throw new RuntimeException("error when getting the files..." + Arrays.toString(e.getStackTrace()));
		}
	}
}
