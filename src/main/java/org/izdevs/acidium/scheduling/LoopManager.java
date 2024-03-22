package org.izdevs.acidium.scheduling;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class LoopManager {
    @Getter
    static Set<ScheduledTask> tasks = new HashSet<>();

    public static void registerTask(ScheduledTask task){
        tasks.add(task);
    }

    public static void scheduleAsyncDelayedTask(int ticks,ScheduledTask task){

    }
}
