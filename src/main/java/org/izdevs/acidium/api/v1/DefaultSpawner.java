package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.entity.AbstractMobSpawner;
import org.izdevs.acidium.entity.MobHolder;
import org.izdevs.acidium.serialization.Resource;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DefaultSpawner extends Resource implements AbstractMobSpawner {
    public static List<Mob> jobQueue = new ArrayList<>();
    @Override
    public void spawn() {

    }

    @Override
    public void tick() {

    }
    public DefaultSpawner(){
        super("DefaultSpawner",false);
        this.setFlags(new ArrayList<>());
        this.getFlags().add("spawner");
        MobHolder.register(this);
    };

}
