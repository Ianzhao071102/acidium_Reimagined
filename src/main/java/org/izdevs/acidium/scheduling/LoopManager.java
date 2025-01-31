package org.izdevs.acidium.scheduling;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.configuration.Config;
import org.izdevs.acidium.tick.TickManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service("loopManager")
public class LoopManager {
    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    ThreadFactory factory = new ThreadFactoryBuilder().setThreadFactory(new AcidThreadFactory()).build();

    //one update at a time, array blocking queue size set to 1
    //todo long-term: finish migration to multi-threading framework
    ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 200, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1), factory);
    @Getter
    volatile Set<DelayedTask> delayedTasks = new HashSet<>();
    @Getter
    //note that these are repeating repeating_tasks
    volatile Set<ScheduledTask> repeating_tasks = new HashSet<>();

    @Autowired
    Set<Ticked> tick_tasks;

    @Getter
    @Setter
    /**
     * is ticking paused
     */
            boolean paused = false;

    public void registerRepeatingTask(ScheduledTask task) {
        logger.debug("repeating task has been registered: " + task.id);
        repeating_tasks.add(task);
    }

    public void scheduleAsyncDelayedTask(DelayedTask task) {
        logger.debug("async delayed task has been registered: " + task.getId());
        delayedTasks.add(task);
    }

    public void pulse() throws Exception {
        for (ScheduledTask task : delayedTasks) {
            if (task.state == ScheduledTask.State.FINISHED) {
                delayedTasks.remove(task);
                break;
            } else {
                if (task.state == ScheduledTask.State.SCHEDULED_WAITING) {
                    if (task.destTick == 0) {
                        if (task.async) {
                            executor.execute(task.task);
                        } else {
                            task.exec();
                            task.state = ScheduledTask.State.FINISHED;
                        }
                    } else {
                        task.destTick--;
                    }
                } else {
                    if (task.state == ScheduledTask.State.EXCEPTION) {
                        delayedTasks.remove(task);
                        throw new RuntimeException("Task:" + task.getId() + "thrown an exception while executing");
                    }
                }
            }
        }

        for (ScheduledTask task : repeating_tasks) {
            if (task.state == ScheduledTask.State.SCHEDULED_WAITING) {
                if (task.destTick == 0) {
                    if (task.async) {
                        executor.execute(task.task);
                        task.state = ScheduledTask.State.FINISHED;
                    } else {
                        task.exec();
                        task.destTick = task._indicator;
                    }
                } else {
                    task.destTick--;
                }
            } else if (task.state == ScheduledTask.State.EXCEPTION) {
                repeating_tasks.remove(task);
                throw new Exception("Repeating Task:" + task.getId() + " Exited with exception");
            }
        }
    }

    @Scheduled(fixedDelay = 1000 / Config.ticksPerSecond)
    public void tick() throws Exception {
        if (!paused) {
            TickManager.stepTick();
            pulse();
        }
    }

    /**
     * schedules every component instanceof Ticked
     */
    @PostConstruct
    public void registerAllTickedTasks() {
        for (Ticked ticked : tick_tasks) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ticked.tick();
                }
            };

            this.repeating_tasks.add(new ScheduledTask(runnable));
        }
    }
}
