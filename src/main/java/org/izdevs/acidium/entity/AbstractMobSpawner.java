package org.izdevs.acidium.entity;

import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.tick.Ticked;

import java.util.ArrayList;
import java.util.List;

public interface AbstractMobSpawner extends Ticked{
    void spawn();
    void tick();
    List<Mob> jobQueue = new ArrayList<>();
    default void addJob(Mob mob){
        jobQueue.add(mob);
    }
    default void deleteJob(Mob mob){
        jobQueue.remove(mob);
    }
    void spawnManual(Mob mob);
}
