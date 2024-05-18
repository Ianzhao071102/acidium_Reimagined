package org.izdevs.acidium.game.entity.spawner;

import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.tick.Ticked;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Ticked
public interface AbstractMobSpawner{
    Entity spawn();
    Entity spawnManual(Mob mob);
}
