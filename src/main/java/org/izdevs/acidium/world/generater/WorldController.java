package org.izdevs.acidium.world.generater;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.event.EntityDeathEvent;
import org.izdevs.acidium.game.entity.spawner.DefaultSpawner;
import org.izdevs.acidium.scheduling.Ticked;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.WorldDataHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.TimeZone;

@Service
@Component
public class WorldController implements Ticked {
    @Autowired
    Set<WorldGenerator> generator;

    @Autowired
    ApplicationEventPublisher publisher;
    public ArrayList<World> worlds = new ArrayList<>();

    public void generateWorld(long seed) {
        boolean y = false;
        for(WorldGenerator gen : generator){
            if(gen.name.equalsIgnoreCase("default")){
                World world = new World();
                world.setSpawner(new DefaultSpawner());
                WorldDataHolder.data.put(world,gen.generate(seed));
                worlds.add(world);
                y = true;
                break;
            }
        }

        //unlikely
        if(!y){
            throw new NoClassDefFoundError("please define a world generator with name: default");
        }
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
