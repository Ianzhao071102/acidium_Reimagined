package org.izdevs.acidium.game.entity.spawner;

import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.basic.Entity;
import org.springframework.stereotype.Component;

@Component
//this class is for test only
public class RevokerSpawner implements AbstractMobSpawner{
    @Override
    public Entity spawn() {
        return null;
    }

    @Override
    public Entity spawnManual(Mob mob) {
        return null;
    }
}
