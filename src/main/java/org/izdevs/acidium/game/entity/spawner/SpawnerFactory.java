package org.izdevs.acidium.game.entity.spawner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SpawnerFactory {
    @Autowired
    @Qualifier("revokerSpawner")
    AbstractMobSpawner revokerSpawner;

    @Autowired
    @Qualifier("defaultSpawner")
    AbstractMobSpawner defaultSpawner;

    public AbstractMobSpawner getSpawner(SpawnerType type){
        switch(type){
            case Default -> {
                return defaultSpawner;
            }
            case Revoker -> {
                return revokerSpawner;
            }
            default -> {
                throw new IllegalArgumentException("this is unreachable");
            }
        }
    }

}
