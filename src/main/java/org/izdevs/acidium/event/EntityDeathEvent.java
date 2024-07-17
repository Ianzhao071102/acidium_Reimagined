package org.izdevs.acidium.event;


import org.izdevs.acidium.basic.Entity;
import org.springframework.context.ApplicationEvent;

public class EntityDeathEvent extends ApplicationEvent {
    long timestamp;

    public EntityDeathEvent(Entity source,long timestamp) {
        super(source);
        this.timestamp = timestamp;
    }
}
