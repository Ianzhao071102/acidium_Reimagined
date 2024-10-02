package org.izdevs.acidium.game.equipment.definitions;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.mechanics.brain.Brain;

public class SinWavePointBrain extends Brain {
    @Override
    public void exec_behaviour(BrainBehaviourKey behaviour) {
        if(behaviour == BrainBehaviourKey.__SPECIAL_WEAPON_PROJECTILE){

        }
    }

    @Override
    public boolean shouldTarget(Entity entity) {
        return false;
    }
}
