package org.izdevs.acidium.world.generater;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.event.EntityDeathEvent;
import org.izdevs.acidium.game.entity.spawner.DefaultSpawner;
import org.izdevs.acidium.scheduling.Ticked;
import org.izdevs.acidium.world.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.TimeZone;

@Service
@Component
public class WorldController implements Ticked {
    @Autowired
    WorldGenerator generator;

    @Autowired
    ApplicationEventPublisher publisher;
    public static ArrayList<World> worlds = new ArrayList<>();

    public void generateWorld(long seed) {
        //still uses default world generator just for now...
        World world = generator.generate(seed);
        world.setSpawner(new DefaultSpawner());
        worlds.add(world);
    }

    @Override
    public void tick() {
        for (int i = 0; i <= worlds.size() - 1; i++) {
            for (int j = 0; j <= worlds.size() - 1; j++) {
                Entity entity = worlds.get(i).getMobs().get(j);
                if (!entity.isAlive()) {
                    publisher.publishEvent(new EntityDeathEvent(entity, System.currentTimeMillis()));
                    worlds.get(i).getMobs().remove(entity);
                }
            }
        }
    }
}
