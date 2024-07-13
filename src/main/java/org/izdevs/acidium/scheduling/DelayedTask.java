package org.izdevs.acidium.scheduling;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class DelayedTask extends ScheduledTask{
    @Id
    @GeneratedValue
    private int id;

    public DelayedTask(Runnable task, long destTick) {
        super(task);
        this.destTick = destTick;
    }
    public DelayedTask(Runnable task,long destTick,boolean async) {
        super(task);
        this.destTick = destTick;
        this.async = async;
    }
}
