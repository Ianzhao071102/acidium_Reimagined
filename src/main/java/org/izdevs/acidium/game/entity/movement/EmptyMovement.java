package org.izdevs.acidium.game.entity.movement;

import com.badlogic.gdx.math.Vector2;
import org.izdevs.acidium.world.Location;

public class EmptyMovement extends Movement{
    public EmptyMovement(Location origin, Vector2 direction) {
        super(null,null);
    }
}
