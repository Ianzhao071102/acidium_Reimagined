package org.izdevs.acidium.game.entity.petals;

import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.CentralUtil;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.movement.Movement;
import org.izdevs.acidium.game.entity.movement.MovementBehaviour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Slf4j
@Component
public class PetalMovementController implements MovementBehaviour {
    @Autowired
    CentralUtil util;
    @Override
    public Movement desired(Entity entity, Movement movement) {
        //todo make this movement follow the opposite direction of the players movement in the future
        SecureRandom random = new SecureRandom();
        float a,b;
        a = random.nextFloat(0,4);
        b = random.nextFloat(0,3);
        movement.direction.add(a,b);
        return movement;
    }
}
