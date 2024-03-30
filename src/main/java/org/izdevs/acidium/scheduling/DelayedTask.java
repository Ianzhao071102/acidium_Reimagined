package org.izdevs.acidium.scheduling;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DelayedTask extends ScheduledTask{
    private Runnable update;
    /**
     * should it run for only once before being destroyed
     */
    @Getter
    @Setter
    boolean single;

    /**
     * the ticks it should wait before being executed
     */
    public long destTick;

    public DelayedTask(Runnable task,long destTick) {
        super(task);
        this.destTick = destTick;
    }

    public DelayedTask(Runnable task,long destTick,boolean single) {
        super(task);
        this.destTick = destTick;
        this.single = single;
    }
    public DelayedTask(Runnable task,long destTick,boolean single, Runnable update) {
        super(task);
        this.destTick = destTick;
        this.single = single;
        this.update = update;
    }

    public DelayedTask(Runnable task,long destTick, Runnable update) {
        super(task);
        this.destTick = destTick;
        this.update = update;
    }
    public void update(){
        if(this.update != null) this.update.run();
    }
}
