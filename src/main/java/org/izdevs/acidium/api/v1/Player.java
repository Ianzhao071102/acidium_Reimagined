package org.izdevs.acidium.api.v1;

import lombok.Getter;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;

import java.util.UUID;


public class Player extends Resource {
    String username;
    UUID uuid;

    public Player(User user) {
        super("player",false);
        UUID uuid = null;
        String username = null;
        try {
            for (int i = 0; i <= this.spec.size(); i++) {
                SpecObject object = this.spec.get(i);
                if (object.getKey().equals("uuid")) {
                    uuid = UUID.fromString((String) object.getValue());
                }else if(object.getKey().equals("username")) {
                    username = (String) object.getValue();
                }
            }
        }catch(Throwable e){
            throw new RuntimeException(e + "please contact maintainer of this class:" + Player.class + User.class + "with underlying : " + Resource.class);
        }
        this.uuid = uuid;
        this.username = username;
    }
    public Player(){
        super("player",false);
        this.username = "unset";
        this.uuid = UUID.randomUUID();
    }
}
