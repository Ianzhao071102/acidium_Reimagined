package org.izdevs.acidium.game.entity.mechanics;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.petals.PetalEntity;
import org.izdevs.acidium.world.World;

import java.util.Set;

public class LivingEntity extends Entity {
    public Set<PetalEntity> petals;
    public void updatePetalAnchors(){
        for(PetalEntity p:petals){
            p.updateAnchor(this);
        }
    }
    public LivingEntity(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
    }
}
