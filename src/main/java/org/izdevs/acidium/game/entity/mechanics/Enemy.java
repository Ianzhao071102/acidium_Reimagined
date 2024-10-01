package org.izdevs.acidium.game.entity.mechanics;

import org.izdevs.acidium.utils.RandomUtils;
import org.izdevs.acidium.world.World;

public class Enemy extends LivingEntity {
    static String generateName(){
        return "ENEMY-" + RandomUtils.getRandomString(5);
    }
    public Enemy(World world, double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(world, generateName(), movementSpeed, health, hitboxRadius, bDamage);
    }
}
