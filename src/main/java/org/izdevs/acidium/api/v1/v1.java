package org.izdevs.acidium.api.v1;

import lombok.Getter;
import org.izdevs.acidium.serialization.API;

public class v1 extends API {
    public v1() {
        super("v1","v1",new User(),new Player(),new Mob(),new Entity(),new DefaultSpawner());
    }
    @Getter
    public enum defined{
        USER,PLAYER,MOB,ENTITY,DEFAULT_SPAWNER
    }
}
