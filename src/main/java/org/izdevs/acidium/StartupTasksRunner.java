package org.izdevs.acidium;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import org.izdevs.acidium.game.equipment.EquipmentHolder;
import org.izdevs.acidium.serialization.*;
import org.izdevs.acidium.tick.TickManager;
import org.izdevs.acidium.utils.ReflectUtil;
import org.izdevs.acidium.world.generater.WorldController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Connection;

import static org.izdevs.acidium.AcidiumApplication.readAndPrintNote;

@Component
public class StartupTasksRunner implements ApplicationRunner {
    public static MessageDispatcher dispatcher = null;
    public static Connection SQLConnection = null;
    static Logger logger = LoggerFactory.getLogger(StartupTasksRunner.class);
    @Autowired
    public boolean generateWorld;
    @Autowired
    @Qualifier("maxPlayers")
    public int maxPlayers;
    @Autowired
    @Qualifier("psql")
    public DataSource dataSource;
    @Autowired
    WorldController controller;
    @Autowired
    SerializerFactory factory;
    @Autowired
    @Qualifier("credits")
    String credits;

    @Autowired
    ResourceFacade facade;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
            credits below
         */
        logger.info("MADE BY: " + credits);
        logger.info("OPENSOURCE ON GITHUB: https://github.com/Ianzhao071102/acidium_Reimagined");
        logger.info("using spring framework...");
        logger.info("Thanks for dphater for inspiring this project...");

        //initialization below
        TickManager.init();


        //handle nbt resources
        org.springframework.core.io.Resource[] resource = ReflectUtil.getNBTResources();
        if (resource.length == 0) {
            logger.info(resource.length + " resources was/were found");
            logger.debug("no nbt file found on classpath");
        }
        for (int i = 0; i <= resource.length - 1; i++) {
            org.springframework.core.io.Resource resource1 = resource[i];
            InputStream stream = resource1.getInputStream();
            ResourceDeserializer deserializer = factory.getDeserializer(DeserializerTypes.NBT);
            Resource selected = deserializer.deserialize(stream);
            facade.registerResource(selected);
            logger.info("registered nbt def: " + resource1.getFilename());
        }

        //handle json resources
        resource = ReflectUtil.getJSONResources();

        if (resource.length == 0) {
            logger.debug("no json file found on classpath");
        }
        for (int i = 0; i <= resource.length - 1; i++) {
            org.springframework.core.io.Resource resource1 = resource[i];
            InputStream stream = resource1.getInputStream();
            ResourceDeserializer deserializer = factory.getDeserializer(DeserializerTypes.JSON);
            Resource selected = deserializer.deserialize(stream);
            facade.registerResource(selected);
            logger.info("registered nbt def: " + resource1.getFilename());
        }

        //call something out
        logger.info("starting resource facade, registering....");
        logger.trace("start world generation...");

        if (generateWorld) {
            SecureRandom seeder = new SecureRandom();
            controller.generateWorld(seeder.nextLong());
            logger.warn("World is being generated...");
        } else {
            logger.warn("world generation is skipped due to configuration");
        }


        logger.info("Messaging for gdxAI is initializing...");
        dispatcher = MessageManager.getInstance();
        logger.info("finished...");

        readAndPrintNote();
        //init later...
        ResourceFacade.start();
        EquipmentHolder.init();
    }
}
