package org.izdevs.acidium.game.entity.movement;

import org.izdevs.acidium.basic.Entity;

public interface MovementBehaviour {
    /**
     * used to intercept movements
     * @param entity
     * @param movement
     */
    Movement desired(Entity entity,Movement movement);
}
