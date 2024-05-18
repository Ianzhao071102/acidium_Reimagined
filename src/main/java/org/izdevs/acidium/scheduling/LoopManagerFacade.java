package org.izdevs.acidium.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoopManagerFacade {
    //facade for non-component classes

    @Autowired
    LoopManager manager;

    public void scheduleAsyncDelayedTask(DelayedTask task){
        manager.registerRepeatingTask(task);
    }
}
