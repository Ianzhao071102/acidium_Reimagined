package org.izdevs.acidium.tick;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.Config;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class TickManager {

    public static long tick = 0;

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
            if(!LoopManager.getDelayedTasks().isEmpty()){
                for(int i = 0; i<=LoopManager.getDelayedTasks().size()-1; i++){
                    DelayedTask task = LoopManager.getDelayedTasks().iterator().next();
                    if(task.getDestTick() == tick) task.exec();
                }
            }
        }
        tick++;

    }
    public static void init(){
        Logger logger = LoggerFactory.getLogger(TickManager.class);
        logger.info("tick manager has been initialized");
        logger.debug("tick manager is not stable...");
    }

}
