package org.izdevs.acidium;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import org.izdevs.acidium.game.crafting.CraftingRecipeHolder;
import org.izdevs.acidium.game.equipment.EquipmentHolder;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.izdevs.acidium.tick.TickManager;
import org.izdevs.acidium.utils.ReflectUtil;
import org.izdevs.acidium.world.WorldController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.List;

import static org.izdevs.acidium.AcidiumApplication.loadNBT;
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
    @Qualifier("credits")
    String credits;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TickManager.init();
        loadNBT();
        logger.info("starting resource facade, registering....");
        logger.trace("start world generation...");

        if (generateWorld) {
            SecureRandom seeder = new SecureRandom();
            WorldController.generateWorld(seeder.nextLong());
            logger.warn("World is being generated...");
        } else {
            logger.warn("world generation is skipped due to configuration");
        }
        //SQL CONNECTION
        try {
            logger.info("trying to connect to sql...");
            SQLConnection = DataSourceUtils.getConnection(dataSource);
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
        logger.info("SQL connection is established with/without exception");

        SQLConnection.createStatement().execute("CREATE TABLE IF NOT EXISTS users (uuid CHARACTER(36),username VARCHAR(21),passwordHash VARCHAR(72))");


        logger.info("Messaging for gdxAI is initializing...");
        dispatcher = MessageManager.getInstance();
        logger.info("finished...");

        readAndPrintNote();
        //init later...
        ResourceFacade.start();

        //note that crafting recipe holder must start before equipment holder
        CraftingRecipeHolder.init();
        EquipmentHolder.init();


        /*
            credits below
         */
        logger.info("MADE BY: " + credits);
        logger.info("OPENSOURCE ON GITHUB: https://github.com/Ianzhao071102/acidium_Reimagined");
        logger.info("using spring framework...");
        logger.info("Thanks for dphater for inspiring this project...");

        Resource[] resources = ReflectUtil.getResources();
        for (Resource resource : resources) {
            if (resource.getFile().getName().equals("welcome.txt")) {
                List<String> lines = Files.readAllLines(Path.of(resource.getURI()));
                for (String line : lines) {
                    logger.info(line);
                }
            }
            break;
        }

    }
}
