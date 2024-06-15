package org.izdevs.acidium.api.v1;

import lombok.Getter;
import org.izdevs.acidium.game.entity.spawner.DefaultSpawner;
import org.izdevs.acidium.serialization.API;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class v1 extends API {
    @Autowired
    ResourceFacade facade;

    public v1() {
        super("v1","v1",new User(),new Player(),new Mob(),new DefaultSpawner(),new BlockSpec(),new Error());
        this.isApi = true;
        this.setName("v1");

        this.register();
        facade.registerAPI(this);
    }
    @Getter
    public enum defined{
        USER,PLAYER,MOB,ENTITY,DEFAULT_SPAWNER
    }
}
