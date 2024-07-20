package org.izdevs.acidium;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.google.common.util.concurrent.MoreExecutors;
import org.izdevs.acidium.game.equipment.EquipmentHolder;
import org.izdevs.acidium.scheduling.AcidThreadFactory;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.izdevs.acidium.AcidiumApplication.readAndPrintNote;

@Component
public class StartupTasksRunner implements ApplicationRunner {
    static Logger logger = LoggerFactory.getLogger(StartupTasksRunner.class);
    @Autowired
    @Qualifier("license")
    boolean gen_license;
    @Autowired
    @Qualifier("generateWorld")
    public boolean generateWorld;
    @Autowired
    @Qualifier("maxPlayers")
    public int maxPlayers;
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
        logger.info("Thanks dphater for inspiring this project...");

        //initialization below
        TickManager.init();

        logger.trace("start world generation...");

        if (generateWorld) {
            SecureRandom seeder = new SecureRandom();
            controller.generateWorld(seeder.nextLong());
            logger.warn("World is being generated...");
        } else {
            logger.warn("world generation is skipped due to configuration");
        }
        readAndPrintNote();
    }
}
