package org.izdevs.acidium.game.entity.mechanics;

import org.izdevs.acidium.utils.RandomUtils;
import org.izdevs.acidium.world.World;

public class Enemy extends LivingEntity {
    static String generateName(){
        return "ENEMY-" + RandomUtils.getRandomString(5);
    }
    public Enemy(World world,int health) {
        super(world,generateName(),1D,health,0,300);
        HitBox hb = new DefaultHitbox();
        hb.multiplier = 2;
        super.setHitbox(hb);
    }
}
