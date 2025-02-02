package org.izdevs.acidium.game.entity.spawner;

import org.izdevs.acidium.basic.Entity;


public interface AbstractMobSpawner{
    Entity spawn();
    Entity spawnManual(/** something's gotta be here**/);
}
