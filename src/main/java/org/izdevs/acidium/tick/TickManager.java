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
import java.util.HashSet;
import java.util.Set;

@Component
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


    public static void stepTick() {

        tick++;
    }

    public static void init() {
        Logger logger = LoggerFactory.getLogger(TickManager.class);
        logger.info("tick manager has been initialized");
        logger.info("this means that the static method init() has been called");
        logger.info("Hello World from Acidium");
    }

    @Scheduled(fixedRate = 1000)
    public void updateTPS() {
        //conversion is safe
        tps = (int) (tick - old_tick);
        old_tick = tick;
    }

}
