package org.izdevs.acidium.scheduling;

import lombok.Getter;

@Getter
public class DelayedTask extends ScheduledTask{
    public long destTick;
    public DelayedTask(Runnable task,long destTick) {
        super(task);
        this.destTick = destTick;
    }
}
