package org.izdevs.acidium.game.entity.mechanics;

import com.badlogic.gdx.math.Vector2;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.mechanics.brain.*;
import org.izdevs.acidium.game.entity.movement.Movement;
import org.izdevs.acidium.game.entity.petals.PetalEntity;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.world.Location;
import org.izdevs.acidium.world.World;

import java.util.Set;

public class LivingEntity extends Entity implements EntityBrainAdapter {
    int cursor = 0;
    public Set<PetalEntity> petals;
    Brain brain = new EmptyBrain();
    EntityBrainConnector connector = new DefaultBrainConnector(this);
    public void updatePetalAnchors(){
        for(PetalEntity p:petals){
            p.updateAnchor(this);
        }
    }
    public LivingEntity(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(world, name, movementSpeed, health, hitboxRadius, bDamage);
    }

    @Override
    public void move(Movement commit) {
        Vector2 loc = commit.direction.add((float) this.getX(),(float) this.getY());

        double distance = Math.hypot(loc.x - this.getX(), loc.y - this.getY());
        double ticks = distance/this.getMovementSpeed();

        for(int i=0;i<=ticks-1;i++){
            int finalI = i;
            LoopManager.scheduleDelayedTask(new DelayedTask(() -> {
                loc.setLength((float) (distance/ finalI +1));
                this.setX(loc.x);
                this.setY(loc.y);
            },i));
        }
    }

    @Override
    public void turn(double dest_deg) {
        this.setFacingDir(dest_deg);
    }

    @Override
    public void switchInv(int origin, int dest) {
        throw new UnsupportedOperationException("too lazy to fucking implement");
    }

    @Override
    public Location __getLocation() {
        return new Location(this.getX(),this.getY());
    }

    @Override
    public void use() {
        //todo implement this
    }

    @Override
    public void switchElectronCursor(int v) {
        if(v >= this.getElectronInv().getItems().size()-1){
            throw new IllegalArgumentException("index out of bounds");
        }
        this.cursor = v;
    }
}
