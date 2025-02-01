package org.izdevs.acidium.game.entity.petals;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.mechanics.LivingEntity;
import org.izdevs.acidium.world.World;

import java.security.SecureRandom;

@Slf4j
public class PetalEntity extends Entity {
    Petal petal;

    private boolean shouldSwitchOrbit() {
        SecureRandom random = new SecureRandom();
        boolean[] a = new boolean[20];
        for (int i = 0; i <= a.length - 1; i++) {
            a[i] = random.nextBoolean();
        }
        return a[random.nextInt(0, a.length - 1)];
    }

    /**
     * max: 8F
     * min: 3F
     *
     * @see Petal
     */
    @Getter
    float orbit_level = -1F;
    @Getter
    int rotation_phase;


    double anchor_x, anchor_y;
    public void updateAnchor(Entity owner){
        if(owner instanceof LivingEntity){
            this.anchor_x = owner.getX();
            this.anchor_y = owner.getY();
        }
        //it cant hold petals in this case:
        else throw new UnsupportedOperationException("owner is not LivingEntity, therefore cannot hold petals");
    }
    public PetalEntity(Petal petal, World world) {
        super(world,"petal-" + petal.id, petal.movementSpeed, petal.health, petal.hitboxRadius, petal.bDamage);
    }
}
