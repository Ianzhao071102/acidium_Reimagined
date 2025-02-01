package org.izdevs.acidium.game.entity.mechanics;

public class DefaultHitbox extends HitBox {
    public DefaultHitbox() {
        super();
        super.type = Type.RECTANGULAR;
        super.anchor_x = 0;
        super.anchor_y = 0;
    }

    @Override
    public boolean collide_with(HitBox hitBox) {
        int ox = (this.owner_x + anchor_x) * super.multiplier;
        int hox = (hitBox.owner_x + hitBox.anchor_x) * hitBox.multiplier;
        int oy = (this.owner_y + anchor_y) * super.multiplier;
        int hoy = (hitBox.owner_y + hitBox.anchor_y) * hitBox.multiplier;
        return ox < hox + 1 &&
                ox + 1 > hox &&
                oy < hoy + 1 &&
                oy + 1 > hoy;
    }
}
