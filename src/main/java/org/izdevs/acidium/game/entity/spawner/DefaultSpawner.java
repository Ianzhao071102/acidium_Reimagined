package org.izdevs.acidium.game.entity.spawner;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.MobHolder;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@Component
public class DefaultSpawner extends Resource implements AbstractMobSpawner {
    @Autowired
    MobHolder mobHolder;

    public static List<Mob> jobQueue = new ArrayList<>();

    World world;

    public DefaultSpawner() {
        super("DefaultSpawner","DefaultSpawner");
    }

    public DefaultSpawner(World world) {
        super("DefaultSpawner","DefaultSpawner");
        this.world = world;
    }

    @Override
    public Entity spawn() {
        Random random = new Random();
        int now = random.nextInt(0, mobHolder.registeredMobs.size() - 1);
        Mob mob = mobHolder.registeredMobs.get(now);
        Entity entity = new Entity(mob.getName(),mob.getSpeed(),mob.getHealth(), mob.getHitboxRadius(), mob.getBDamage());
        entity.setX(random.nextInt(0, 3995));
        entity.setY(random.nextInt(0, 3995));
        return entity;
    }

    @Override
    public Entity spawnManual(Mob mob) {
        Random random = new Random();
        Entity entity = new Entity(mob.getName(),mob.getSpeed(),mob.getHealth(), mob.getHitboxRadius(), mob.getBDamage());
        entity.setX(random.nextInt(0, 3995));
        entity.setY(random.nextInt(0, 3995));
        return entity;
    }
}
