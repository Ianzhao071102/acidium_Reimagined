package org.izdevs.acidium.scheduling;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.configuration.Config;
import org.izdevs.acidium.tick.TickManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class LoopManager {
    /**
     * milliseconds between each pulse
     */
    public static final int pulse_interval = 50;
    public static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    @Getter
    static volatile Set<DelayedTask> delayedTasks = new HashSet<>();
    @Getter
    //note that these are repeating repeating_tasks
    static volatile Set<ScheduledTask> repeating_tasks = new HashSet<>();
    @Getter
    @Setter
    /**
     * is ticking paused
     */
    static boolean paused = false;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(AcidThreadFactory.INSTANCE);
    private final ExecutorService async_executor = Executors.newFixedThreadPool(MAX_THREADS, AcidThreadFactory.INSTANCE);

    public static void registerRepeatingTask(ScheduledTask task) {
        repeating_tasks.add(task);
    }

    public static void scheduleAsyncDelayedTask(DelayedTask task) {
        delayedTasks.add(task);
    }

    public void scheduleSyncTask(ScheduledTask task, long time, TimeUnit tu) {
        executor.schedule(task.task, time, tu);
    }

    @Scheduled(fixedDelay = pulse_interval)
    public void pulse() throws Exception {
        for (ScheduledTask task : delayedTasks) {
            if (task.state == ScheduledTask.State.FINISHED) {
                delayedTasks.remove(task);
            } else {
                if (task.state == ScheduledTask.State.SCHEDULED_WAITING) {
                    if (task.destTick == 0) {
                        if (task.async) {
                            async_executor.submit(task.task);
                        } else {
                            task.exec();
                        }
                    } else {
                        task.destTick--;
                    }
                } else {
                    if (task.state == ScheduledTask.State.EXCEPTION) {
                        throw new Exception("Task:" + task.getId() + "thrown an exception while executing");
                    }
                }
            }
        }
    }

    @Scheduled(fixedDelay = 1000 / Config.ticksPerSecond)
    public void tick() {
        if (!paused) {
            TickManager.stepTick();
        }
    }
}
