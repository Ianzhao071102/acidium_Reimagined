package org.izdevs.acidium.game.entity.mechanics.brain;

import org.izdevs.acidium.game.entity.movement.Movement;
import org.izdevs.acidium.world.Location;

public interface EntityBrainAdapter {
    void move(Movement commit);
    void turn(double dest_deg);
    void switchInv(int origin,int dest);
    int getHealth();
    Location __getLocation();
    //use the item selected by the cursor
    void use();
    //the cursor that is pointing to the item of the shit
    void switchElectronCursor(int v);
}
