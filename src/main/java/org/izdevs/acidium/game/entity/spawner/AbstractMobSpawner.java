package org.izdevs.acidium.game.entity.spawner;

import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.basic.Entity;


public interface AbstractMobSpawner{
    Entity spawn();
    Entity spawnManual(Mob mob);
}
