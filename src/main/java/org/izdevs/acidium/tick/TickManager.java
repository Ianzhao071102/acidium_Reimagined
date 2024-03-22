package org.izdevs.acidium.tick;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.Config;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class TickManager {
    @Getter
    @Setter
    static boolean paused = false;
    @Scheduled(fixedDelay = 1000/Config.ticksPerSecond)
    public void stepTick(){
        if(!paused){
            for(int i=0;i<=LoopManager.getTasks().size()-1;i++){
                ScheduledTask task = LoopManager.getTasks().iterator().next();
                task.exec();
            }
        }
    }
    public static void init(){
        Logger logger = LoggerFactory.getLogger(TickManager.class);
        logger.info("tick manager has been initialized");
        logger.debug("tick manager is not stable...");
    }

}
