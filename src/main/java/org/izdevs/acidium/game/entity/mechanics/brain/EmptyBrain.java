package org.izdevs.acidium.game.entity.mechanics.brain;

import jakarta.annotation.PostConstruct;
import org.izdevs.acidium.basic.Entity;

import java.util.HashMap;

public class EmptyBrain extends Brain {
    @PostConstruct
    public void a() {
        this.attributes = new HashMap<>();
    }

    @Override
    public void exec_behaviour(BrainBehaviourKey behaviour) {
        //does nothing as the class name suggests
    }

    @Override
    public boolean shouldTarget(Entity entity) {
        //targets nothing as the class name suggests
        return false;
    }
}
