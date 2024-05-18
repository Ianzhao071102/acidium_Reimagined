package org.izdevs.acidium.api.v1;

import lombok.Getter;
import org.izdevs.acidium.entity.spawner.DefaultSpawner;
import org.izdevs.acidium.serialization.API;

import static org.izdevs.acidium.serialization.ResourceFacade.registerAPI;

public class v1 extends API {
    public v1() {
        super("v1","v1",new User(),new Player(),new Mob(),new DefaultSpawner(),new BlockSpec(),new Error());
        this.isApi = true;
        this.setName("v1");

        this.register();
        registerAPI(this);
    }
    @Getter
    public enum defined{
        USER,PLAYER,MOB,ENTITY,DEFAULT_SPAWNER
    }
}
