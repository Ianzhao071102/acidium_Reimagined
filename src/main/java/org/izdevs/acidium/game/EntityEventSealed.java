package org.izdevs.acidium.game;

import org.izdevs.acidium.basic.Entity;
import org.springframework.context.ApplicationEvent;

public class EntityEventSealed extends ApplicationEvent {
    long timestamp;
    public EntityEventSealed(Entity obj1,Entity obj2,long timestamp) {
        super(new Entity[]{obj1, obj2});
        this.timestamp = timestamp;
    }
    public EntityEventSealed(Entity obj,long timestamp){
        super(obj);
        this.timestamp = timestamp;
    }
    public EntityEventSealed(Entity obj){
        super(obj);
        this.timestamp = System.currentTimeMillis();
    }
}
