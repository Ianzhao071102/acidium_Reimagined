package org.izdevs.acidium.tick;

public interface Ticked {
    void tick();
    default void stopTick(){

    }
    default void pause(){

    }
}
