package org.izdevs.acidium.game.entity.mechanics;

import java.util.Set;

public interface CollisionDetector {
    public Set<HitBox> hb_in_collision(HitBox e1);
}
