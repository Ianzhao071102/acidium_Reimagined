package org.izdevs.acidium.game.entity.movement;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import org.izdevs.acidium.game.entity.mechanics.brain.BrainOutputType;
import org.izdevs.acidium.world.Location;

@AllArgsConstructor
public class Movement extends BrainOutputType<Movement> {
    public Location origin;
    public Vector2 direction;

    @Override
    public Movement getValue() {
        return this;
    }
}
