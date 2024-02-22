package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.entity.AbstractMobSpawner;
import org.izdevs.acidium.serialization.Resource;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DefaultSpawner extends Resource implements AbstractMobSpawner {
    public static List<Mob> jobQueue = new ArrayList<>();
    @Override
    public void spawn() {
        //TODO FINISH SPAWNING ALGORITHMS
    }

    @Override
    public void tick() {
        //TODO FINISH SPAWNING ALGORITHMS
    }
    public DefaultSpawner(){
        super("DefaultSpawner",false);
        this.setFlags(new ArrayList<>());
        this.getFlags().add("spawner");
    };

}
