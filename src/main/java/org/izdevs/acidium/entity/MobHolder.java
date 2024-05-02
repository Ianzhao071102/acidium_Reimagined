package org.izdevs.acidium.entity;

import jakarta.annotation.PostConstruct;
import org.izdevs.acidium.api.v1.DefaultSpawner;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;

import java.util.ArrayList;
import java.util.Random;

public class MobHolder{
    //WARNING: PLEASE REGISTER YOUR SPAWNER HERE OR IT WON'T BE CALLED IN THE FUTURE
    public static ArrayList<Mob> registeredMobs = new ArrayList<>();
    public static void registerMob(Mob mob){
        registeredMobs.add(mob);
    }
    public static void removeMob(Mob mob){
        registeredMobs.remove(mob);
    }
    public static ArrayList<AbstractMobSpawner> registeredSpawners = new ArrayList<>();

    public void tick() {
        Random random = new Random();
        int index = random.nextInt(0, registeredMobs.size()-1);
        //now it only supports scheduling mobs through default spawner
        Mob mob = registeredMobs.get(index);
        DefaultSpawner.jobQueue.add(mob);
    }
    public static void registerSpawner(AbstractMobSpawner spawner){
        registeredSpawners.add(spawner);
    }

    @PostConstruct
    public void init(){
        LoopManager.registerRepeatingTask(new ScheduledTask(this::tick));
    }
}
