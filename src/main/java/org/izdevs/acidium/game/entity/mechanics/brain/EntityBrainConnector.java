package org.izdevs.acidium.game.entity.mechanics.brain;

import lombok.AllArgsConstructor;
import org.izdevs.acidium.basic.Entity;

import java.lang.ref.Reference;
@AllArgsConstructor
public abstract class EntityBrainConnector {
    Entity owner;
    public abstract void commitBrainOutput(BrainOutput<? extends BrainOutputType<?>> output);
}
