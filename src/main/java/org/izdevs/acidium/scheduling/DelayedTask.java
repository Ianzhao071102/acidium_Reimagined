package org.izdevs.acidium.scheduling;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
public class DelayedTask extends ScheduledTask{
    public DelayedTask(Runnable task,long destTick) {
        super(task);
        this.destTick = destTick;
    }
    public DelayedTask(Runnable task,long destTick,boolean async) {
        super(task);
        this.destTick = destTick;
        this.async = async;
    }
}
