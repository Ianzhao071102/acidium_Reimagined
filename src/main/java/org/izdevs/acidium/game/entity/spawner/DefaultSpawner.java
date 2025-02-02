package org.izdevs.acidium.game.entity.spawner;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.World;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class DefaultSpawner extends Resource implements AbstractMobSpawner {
    World world;

    public DefaultSpawner() {
        super("DefaultSpawner","DefaultSpawner");
    }

    public DefaultSpawner(World world) {
        super("DefaultSpawner","DefaultSpawner");
        this.world = world;
    }

    @Override
    public Entity spawn() {
        //todo rewrite spawner logic
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity spawnManual() {
        //todo rewrite spawner logic
        throw new UnsupportedOperationException();
    }
}
