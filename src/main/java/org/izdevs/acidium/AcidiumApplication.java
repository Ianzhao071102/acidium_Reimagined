package org.izdevs.acidium;


import org.izdevs.acidium.networking.Server;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.izdevs.acidium.tick.TickManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

import static org.izdevs.acidium.serialization.NBTParser.registerNBTDef;

@Configuration
@SpringBootApplication
@EnableScheduling
@EntityScan("org.izdevs.acidium")
public class AcidiumApplication{

	@Autowired
	int maxPlayers;

	@Autowired
	static DataSource dataSource;

	public static Connection SQLConnection = null;

	public static String version = "alpha-0.1";
	static Logger logger = LoggerFactory.getLogger(AcidiumApplication.class);
	static ArrayList<Resource> resources = new ArrayList<>();
	public static void main(String[] args) throws Throwable {
		//REGISTER RESOURCES
		SpringApplication.run(AcidiumApplication.class, args);
		TickManager.init();
		loadNBT();
		logger.info("starting resource facade, registering....");
		ResourceFacade.start();



		//PORT
		int port = 0;
		boolean random = true;
		if(args.length>0){
			try{
				port = Integer.parseInt(args[0]);
				random = false;
			}catch(Throwable e){
				logger.warn("error when parsing port(first argument)");
			}
		}
		else{
            port = getFreePort();
		}

		if(random){
			logger.warn("using free port:" + port);
		}
		Server server = new Server(port);
		if (random) logger.warn("Server is running in randomized port: " + port);
		else logger.warn("server running on port: " + port);
		server.start();


		//SQL CONNECTION
		try {
			SQLConnection = DataSourceUtils.getConnection(dataSource);
		}catch(Throwable e){
			logger.error(e.getMessage());
		}
		logger.info("SQL connection is established with/without exception");
		SQLConnection.createStatement().execute("CREATE TABLE IF NOT EXISTS users (uuid CHARACTER(36),username VARCHAR(21),passwordHash VARCHAR(72))");


	}
	public static void loadNBT() throws IOException {
		org.springframework.core.io.Resource[] resource = getXMLResources();
		if(resource.length == 0){
			logger.info(resource.length + " resources was/were found");
			logger.debug("no nbt file found on classpath");
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
	private static org.springframework.core.io.Resource[] getXMLResources() {
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
	public static int getFreePort() {
		try (ServerSocket socket = new ServerSocket(0)) {
			socket.setReuseAddress(true);
			return socket.getLocalPort();
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	public static String bcrypt(String string){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.encode("myPassword");
	}
}
