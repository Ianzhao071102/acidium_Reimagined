package org.izdevs.acidium;


import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import org.izdevs.acidium.networking.Server;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.izdevs.acidium.tick.TickManager;
import org.izdevs.acidium.world.WorldController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.izdevs.acidium.serialization.NBTParser.registerNBTDef;

@Configuration
@SpringBootApplication
@EnableScheduling
@EntityScan("org.izdevs.acidium")
public class AcidiumApplication {
    public static MessageDispatcher dispatcher = null;

    @Autowired
    @Qualifier("port")
    static int port;

    @Autowired
    @Qualifier("maxPlayers")
    int maxPlayers;

    @Autowired
//	@Qualifier("psql")
    static DataSource dataSource;

    public static Connection SQLConnection = null;

    public static String version = "alpha-0.1";
    static Logger logger = LoggerFactory.getLogger(AcidiumApplication.class);
    static ArrayList<Resource> resources = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        //REGISTER RESOURCES
        SpringApplication.run(AcidiumApplication.class, args);
        TickManager.init();
        loadNBT();
        logger.info("starting resource facade, registering....");


        logger.trace("start world generation...");
        SecureRandom seeder = new SecureRandom();
        WorldController.generateWorld(seeder.nextLong());
        logger.warn("World is being generated...");

        //PORT
        boolean random = true;
        if (port != 0) {
            random = false;
        } else {
            port = getFreePort();
        }

        if (random) {
            logger.warn("using free port:" + port);

        }
        Server server = new Server(port);
        if (random) logger.warn("Server is running in randomized port: " + port);
        else logger.warn("server running on port: " + port);

        try {
            logger.info("start of server is being tried...");
            server.start();
        } catch (Throwable e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info("started server...");

        //SQL CONNECTION
        try {
            logger.info("trying to connect to sql...");
            logger.info(dataSource.toString());
            SQLConnection = DataSourceUtils.getConnection(dataSource);
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
        logger.info("SQL connection is established with/without exception");
        SQLConnection.createStatement().execute("CREATE TABLE IF NOT EXISTS users (uuid CHARACTER(36),username VARCHAR(21),passwordHash VARCHAR(72))");


        logger.info("Messaging for gdxAI is initializing...");
        dispatcher = MessageManager.getInstance();
        logger.info("finished...");


        //init later...
        ResourceFacade.start();
    }

    public static void loadNBT() throws IOException {
        org.springframework.core.io.Resource[] resource = getXMLResources();
        if (resource.length == 0) {
            logger.info(resource.length + " resources was/were found");
            logger.debug("no nbt file found on classpath");
            return;
        }
        for (int i = 0; i <= resource.length - 1; i++) {
            org.springframework.core.io.Resource resource1 = resource[i];
            ;
            try (InputStream stream = new FileInputStream(resource1.getFile())) {
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
            for (org.springframework.core.io.Resource r : metaInfResources) {
                logger.info(r.getURI() + " found resource nbt");
                logger.info(r.getURI() + "is being loaded....");
            }
            return metaInfResources;
        } catch (Throwable e) {
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

    public static String bcrypt(String string) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.encode(string);
    }


    public double distance(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public static void readAndPrintNote() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            org.springframework.core.io.Resource[] metaInfResources = resourcePatternResolver
                    .getResources("classpath*:*.note");
            for (org.springframework.core.io.Resource r : metaInfResources) {
                logger.info(r.getURI() + " found node");
                List<String> lines = Files.readAllLines(Path.of(r.getURI()));
                logger.info("--------- BEGIN NOTE --------------");
                for (int j = 0; j <= lines.size() - 1; j++) {
                    logger.info(lines.get(j));
                }
                logger.info("--------- END NOTE ----------------");
            }
        } catch (Throwable e) {
            throw new RuntimeException("error when getting the files..." + Arrays.toString(e.getStackTrace()));
        }
    }
}
