package org.izdevs.acidium.game.entity.mechanics;

import org.izdevs.acidium.world.World;

public abstract class HitBox {
    World world;
    int multiplier = 1;
    enum Type{
        RECTANGULAR,
        CIRCULAR
    }
    Type type;
    int anchor_x, anchor_y;
    public abstract boolean collide_with(HitBox hitBox);
    int owner_x, owner_y;
    public void update(int x, int y, World world){
        this.owner_x = x;
        this.owner_y = y;
        this.world = world;
    }
}
