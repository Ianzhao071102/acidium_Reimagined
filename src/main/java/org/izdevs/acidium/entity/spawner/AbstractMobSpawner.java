package org.izdevs.acidium.entity.spawner;

import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.tick.Ticked;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Ticked
public interface AbstractMobSpawner{
    void spawn();
    List<Mob> jobQueue = new ArrayList<>();
    default void addJob(Mob mob){
        jobQueue.add(mob);
    }
    default void deleteJob(Mob mob){
        jobQueue.remove(mob);
    }
    void spawnManual(Mob mob);
    default void spawnLoop(){
        int interval = new Random().nextInt(0,15);
        DelayedTask task1 = new DelayedTask(this::spawn,interval);
    }
}
