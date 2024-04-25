package org.izdevs.acidium.entity;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.izdevs.acidium.basic.UserRepository;
import org.izdevs.acidium.world.WorldController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.izdevs.acidium.utils.LoggingUtils.panic;

@Getter
public class EntityHolder {
    Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostConstruct
    public void initialize(UserRepository repository) {
        logger.info("initializing entity holder...");
        if (repository == null) panic("could not find entity repository", this.getClass());
        else {
            logger.info("initialized entity holder...");
        }
    }

    public static long getCountEntitiesAlive() {
        long value = 0;
        for (int i = 0; i <= WorldController.worlds.size() - 1; i++) {
            for (int j = 0; j <= WorldController.worlds.get(i).mobs.size() - 1; j++) value++;
        }

        return value;
    }
}
