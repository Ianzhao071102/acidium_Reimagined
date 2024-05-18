package org.izdevs.acidium.game.entity;

import jakarta.annotation.PostConstruct;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.game.entity.spawner.AbstractMobSpawner;
import org.izdevs.acidium.game.entity.spawner.DefaultSpawner;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

@Component
public class MobHolder{
    public ArrayList<Mob> registeredMobs = new ArrayList<>();
    public void registerMob(Mob mob){
        registeredMobs.add(mob);
    }
    public void removeMob(Mob mob){
        registeredMobs.remove(mob);
    }
    public ArrayList<AbstractMobSpawner> registeredSpawners = new ArrayList<>();
    public void registerSpawner(AbstractMobSpawner spawner){
        registeredSpawners.add(spawner);
    }
}
