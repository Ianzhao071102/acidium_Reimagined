package org.izdevs.acidium.game.entity.mechanics.brain;

import com.badlogic.gdx.math.Vector2;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.mechanics.LivingEntity;
import org.izdevs.acidium.game.entity.movement.Movement;
import org.izdevs.acidium.world.generater.WorldController;

public class DefaultBrainConnector extends EntityBrainConnector {
    public DefaultBrainConnector(Entity owner) {
        super(owner);
    }

    @Override
    public void commitBrainOutput(BrainOutput<? extends BrainOutputType<?>> output) {
        if (output.value() instanceof Movement) {
            LivingEntity dest = (LivingEntity) this.owner;

            Vector2 v = new Vector2(((Movement) output.value()).direction);
            v.add((float) ((Movement) output.value()).origin.getX(), (float) ((Movement) output.value()).origin.getY());

            WorldController.patchEntity(this.owner.getWorld().getName(),this.owner,dest);
        }
        //TODO IMPLEMENT MORE BRAIN OUTPUT TYPES AND ADD THE DEFAULT HANDLERS HERE
    }
}
