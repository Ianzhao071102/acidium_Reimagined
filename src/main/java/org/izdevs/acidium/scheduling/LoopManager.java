package org.izdevs.acidium.scheduling;

import lombok.Getter;
import org.izdevs.acidium.tick.TickManager;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class LoopManager {


    @Getter
    static volatile Set<DelayedTask> delayedTasks = new HashSet<>();
    @Getter
    static volatile Set<ScheduledTask> tasks = new HashSet<>();

    public static void registerTask(ScheduledTask task){
        tasks.add(task);
    }

    public static void scheduleAsyncDelayedTask(DelayedTask task){
        delayedTasks.add(task);
    }
}
