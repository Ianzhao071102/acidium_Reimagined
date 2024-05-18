package org.izdevs.acidium.entity.spawner;

import org.izdevs.acidium.api.v1.Mob;
import org.springframework.stereotype.Component;

@Component
public class RevokerSpawner implements AbstractMobSpawner{
    @Override
    public void spawn() {

    }

    @Override
    public void spawnManual(Mob mob) {

    }

    @Override
    public void spawnLoop() {
        AbstractMobSpawner.super.spawnLoop();
    }
}
