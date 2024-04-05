package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.entity.AbstractMobSpawner;
import org.izdevs.acidium.entity.MobHolder;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class DefaultSpawner extends Resource implements AbstractMobSpawner {
    World world;
    public static List<Mob> jobQueue = new ArrayList<>();
    @Override
    public void spawn() {
        Random random = new Random();
        int now = random.nextInt(0,MobHolder.registeredMobs.size()-1);
        Mob mob = MobHolder.registeredMobs.get(now);
        mob.setX(random.nextInt(0,3995));
        mob.setY(random.nextInt(0,3995));
        world.addMob(mob);
    }


    public void tick() {
        DelayedTask task = new DelayedTask(() -> {
            Random random = new Random();
            long sleep = random.nextLong(20L,500L);
            try {
                Thread.sleep(sleep);
                spawn();
            }catch(InterruptedException ignored){
            }
        },500,false);

        LoopManager.scheduleAsyncDelayedTask(task);
    }

    @Override
    public void spawnManual(Mob mob) {

    }

    public DefaultSpawner(){
        super("DefaultSpawner",false);
        this.setFlags(new ArrayList<>());
        this.getFlags().add("spawner");
        MobHolder.register(this);
    }

    public DefaultSpawner(World world){
        super("DefaultSpawner",false);
        this.world = world;
    }
}
