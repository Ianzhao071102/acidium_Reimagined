package org.izdevs.acidium.tick;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.Metrics;
import org.izdevs.acidium.configuration.Config;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Component
@ComponentScan(basePackages = "org.izdevs.acidium")
public class TickManager {
    /**
     * newly collected ticks per second value
     */
    public static int tps;

    /**
        the tick per second collected a second ago
     */
    public static long old_tick;

    /**
        the current tick
     */
    public static long tick = 0;

    @Getter
    @Setter
    static boolean paused = false;

    @Scheduled(fixedDelay = 1000 / Config.ticksPerSecond)
    public static void stepTick() {
        if (!paused) {
            for (int i = 0; i <= LoopManager.getTasks().size() - 1; i++) {
                ScheduledTask task = LoopManager.getTasks().iterator().next();
                task.exec();
            }
            if (!LoopManager.getDelayedTasks().isEmpty()) {
                for (int i = 0; i <= LoopManager.getDelayedTasks().size() - 1; i++) {
                    DelayedTask task = LoopManager.getDelayedTasks().iterator().next();
                    if (task.getDestTick() == tick) {
                        task.exec();

                        //is it single timed
                        if (task.isSingle()) {
                            LoopManager.getDelayedTasks().remove(task);
                        } else {
                            //adds the dest tick to make sure the task runs again
                            long dest_tick_added = task.destTick;
                            task.destTick += dest_tick_added;
                        }

                    }
                }
            }
            tick++;
            Metrics.ticksElapsed.increment(1);
        }
    }

    public static void init() {
        Logger logger = LoggerFactory.getLogger(TickManager.class);
        logger.info("tick manager has been initialized");
    }

    @Scheduled(fixedRate = 1000)
    public void updateTPS() {
        //conversion is safe
        tps = (int) (tick - old_tick);
        old_tick = tick;
    }

}
