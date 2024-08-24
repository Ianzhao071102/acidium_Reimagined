package org.izdevs.acidium.game.entity;

import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.world.generater.WorldController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class EntityWorldAssigner {
    @Autowired
    WorldController controller;

    /**
     * @param entity the entity that needs to be assigned
     * @return the name of the world that entity is being assigned to
     */
    public String assign(Entity entity) {
        if (entity.isAlive() && !entity.getWorld_name().isEmpty()) {
            int a = new Random().nextInt(0, controller.worlds.size() - 1);
            String name = controller.worlds.get(a).getName();
            log.debug("assigned entity to world name: " + name);
            return name;
        } else {
            log.info("one entity cannot be assigned a world, setting world_name as __WAITING__");
            return "__WAITING__";
        }
    }
}
