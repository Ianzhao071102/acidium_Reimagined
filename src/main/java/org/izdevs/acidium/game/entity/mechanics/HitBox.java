package org.izdevs.acidium.game.entity.mechanics;

public abstract class HitBox {
    enum Type{
        RECTANGULAR,
        CIRCULAR
    }
    Type type;
    int anchor_x, anchor_y;
    public abstract boolean collide_with(HitBox hitBox);
    int owner_x, owner_y;
    public void update(int x,int y){
        this.owner_x = x;
        this.owner_y = y;
    }
}
