package org.izdevs.acidium.game.equipment.definitions;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.world.Location;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.WorldDataHolder;

public class SinWave extends Equipment {
    int cd_current = cd_max;
    public static final int cd_max = 200;

    public SinWave(World world, Entity owner, int maxDurability) {
        super(maxDurability,owner);
        this.setOwner(owner);
    }

    @Override
    public boolean is_cd_over() {
        return (cd_current == 0);
    }

    public void handleBroken() {
        this.cd_current = Equipment.reserved_cd;
    }
}
