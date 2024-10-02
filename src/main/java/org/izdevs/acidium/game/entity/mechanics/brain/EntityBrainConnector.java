package org.izdevs.acidium.game.entity.mechanics.brain;

public abstract class EntityBrainConnector {
    public abstract void commitBrainOutput(BrainOutput<? extends BrainOutputType<?>> output);
}
