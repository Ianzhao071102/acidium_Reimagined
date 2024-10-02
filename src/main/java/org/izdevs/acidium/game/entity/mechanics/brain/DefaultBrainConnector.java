package org.izdevs.acidium.game.entity.mechanics.brain;

import org.izdevs.acidium.game.entity.movement.Movement;

public class DefaultBrainConnector extends EntityBrainConnector {

    @Override
    public void commitBrainOutput(BrainOutput<? extends BrainOutputType<?>> output) {
        if(output.value() instanceof Movement){
            
        }
    }
}
