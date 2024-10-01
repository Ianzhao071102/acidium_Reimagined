package org.izdevs.acidium.game.equipment.definitions;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.world.Location;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.WorldDataHolder;
import org.izdevs.acidium.world.generater.WorldController;

public class SinWave extends Equipment {
    int cd_current = cd_max;
    public static final int cd_max = 200;

    public SinWave(World world, Entity owner, int maxDurability) {
        super(world, owner.getName(), 0.75, 40, 1, 100, maxDurability);
        this.setOwner(owner);
    }

    @Override
    public boolean is_cd_over() {
        return (cd_current == 0);
    }

    @Override
    public void use() {
        //todo finish this: https://www.desmos.com/calculator/kktseqq0yn
        //todo the point has to move as described

        //summons the entity right in front of the dudes
        for(World world: WorldDataHolder.data.keySet()){
            if(world.getName().equals(this.getWorld().getName())){
                Point a = new Point(world);
                double x = this.getX() + (0.5*Math.cos(this.getFacingDir()));
                double y = this.getX() + (0.5*Math.sin(this.getFacingDir()));
                a.setFacingDir(this.getFacingDir());
                world.summonEntity(a,new Location(x,y));
                break;
            }
        }
    }

    @Override
    public void handleBroken() {
        this.cd_current = Equipment.reserved_cd;
    }
}
