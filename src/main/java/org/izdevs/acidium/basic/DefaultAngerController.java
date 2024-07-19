package org.izdevs.acidium.basic;

import org.izdevs.acidium.api.v1.Player;

public class DefaultAngerController implements AngerController{
    @Override
    public boolean angerAt(Entity entity) {
        return entity instanceof Player && entity.alive;
    }
}
