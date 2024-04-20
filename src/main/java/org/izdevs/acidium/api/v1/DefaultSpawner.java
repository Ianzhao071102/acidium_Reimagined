package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.entity.AbstractMobSpawner;
import org.izdevs.acidium.entity.MobHolder;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter

public class DefaultSpawner extends Resource implements AbstractMobSpawner {
    public static List<Mob> jobQueue = new ArrayList<>();
    World world;

    public DefaultSpawner() {
        super("DefaultSpawner", false);
        this.setFlags(new ArrayList<>());
        this.getFlags().add("spawner");
        MobHolder.registerSpawner(this);
        this.setTickRun(() -> {
            Random random = new Random();
            long sleep = random.nextLong(20L, 500L);
            try {
                Thread.sleep(sleep);
                spawn();
            } catch (InterruptedException ignored) {
            }
        });
    }

    public DefaultSpawner(World world) {
        super("DefaultSpawner", false);
        this.world = world;
        this.setTickRun(() -> {
            Random random = new Random();
            long sleep = random.nextLong(20L, 500L);
            try {
                Thread.sleep(sleep);
                spawn();
            } catch (InterruptedException ignored) {
            }
        });
    }

    @Override
    public void spawn() {
        Random random = new Random();
        int now = random.nextInt(0, MobHolder.registeredMobs.size() - 1);
        Mob mob = MobHolder.registeredMobs.get(now);
        mob.setX(random.nextInt(0, 3995));
        mob.setY(random.nextInt(0, 3995));
        world.addMob(mob);
    }


    @Override
    public void addJob(Mob mob) {
        AbstractMobSpawner.super.addJob(mob);
    }

    @Override
    public void deleteJob(Mob mob) {
        AbstractMobSpawner.super.deleteJob(mob);
    }

    @Override
    public void spawnManual(Mob mob) {
        jobQueue.add(mob);
    }

}
