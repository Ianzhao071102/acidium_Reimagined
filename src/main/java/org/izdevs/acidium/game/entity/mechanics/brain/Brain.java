package org.izdevs.acidium.game.entity.mechanics.brain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.serialization.Resource;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public abstract class Brain extends Resource {
    Map<String,Object> attributes;

    //todo theres gotta be more
    public enum BrainBehaviourKey{
        IDLE,
        ATTACK_ENTITY,
        ATTACK_PLAYER,
        //attacked by entity
        ATTACK_BY_ENTITY,
        //attacked by player
        ATTACK_BY_PLAYER,
        //get damaged by entity
        @Deprecated
        DAMAGE_BY_ENTITY,
        __SPECIAL_WEAPON_PROJECTILE
    }
    public abstract void exec_behaviour(BrainBehaviourKey behaviour);
    public abstract boolean shouldTarget(Entity entity);
}
