package org.izdevs.acidium.game.entity.petals;

import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.CentralUtil;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.movement.Movement;
import org.izdevs.acidium.game.entity.movement.MovementBehaviour;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class PetalMovementController implements MovementBehaviour {
    @Autowired
    CentralUtil util;
    @Override
    public Movement desired(Entity entity, Movement movement) {
        assert entity instanceof PetalEntity;

        double radius = ((PetalEntity) entity).orbit_level;
        double ax = ((PetalEntity) entity).anchor_x;
        double ay = ((PetalEntity) entity).anchor_y;

        if (!(radius < Petal.orbit_radius_min || radius > Petal.orbit_radius_max)) {
            double x = radius * Math.cos(((PetalEntity) entity).getRotation_phase()) + ax;
            double y = radius * Math.sin(((PetalEntity) entity).getRotation_phase()) + ay;

            movement.direction.set((float) x,(float) y);
            return movement;
        } else {
            //error
            log.error("failed to apply movement, entity is not initialized with proper value of orbit_level");
            util.crash("log indicates error",new IllegalStateException("entity state of orbit_level (on petal) is illegal"),false);
        }
        throw new UnsupportedOperationException("how did we get here");
    }
}
