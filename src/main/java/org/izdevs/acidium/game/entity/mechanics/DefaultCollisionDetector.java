package org.izdevs.acidium.game.entity.mechanics;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.world.generater.WorldController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DefaultCollisionDetector implements CollisionDetector {
    @Autowired
    WorldController worldController;

    @Override
    public Set<HitBox> hb_in_collision(HitBox e1) {
        Set<HitBox> hitBoxes = new HashSet<>();
        for (Entity entity : worldController.worlds.get(e1.world.index).mobs) {
            if (entity.getHitbox().collide_with(e1)) {
                hitBoxes.add(entity.getHitbox());
            }
        }
        return hitBoxes;
    }
}
