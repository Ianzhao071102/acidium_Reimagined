package org.izdevs.acidium.tick;
import org.izdevs.acidium.Config;
import org.springframework.scheduling.annotation.Scheduled;

public interface Ticked {
    @Scheduled(fixedDelay = 1000/Config.ticksPerSecond)
    void tick();
    default void stopTick(){

    }
    default void pause(){

    }

}
