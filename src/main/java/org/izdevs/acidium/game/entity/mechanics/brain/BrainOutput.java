package org.izdevs.acidium.game.entity.mechanics.brain;

public abstract class BrainOutput<T> {
    //getting the value of the output
    //for example, an entity moves
    //T should be Movement.class
    public abstract <t extends BrainOutputType<?>> t value();
}
