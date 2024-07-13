package org.izdevs.acidium.scheduling;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TickTaskRegisterer {
    @Autowired
    LoopManager manager;
    @Autowired
    Set<Ticked> ticked;

    @PostConstruct
    public void init(){
        for(int i=0;i<= ticked.size()-1;i++){
            manager.registerRepeatingTask(new ScheduledTask(ticked.iterator().next()::tick));
        }
    }
}
