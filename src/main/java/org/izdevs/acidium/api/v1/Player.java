package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.inventory.Inventory;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;

import java.util.UUID;

@Getter
public class Player extends Entity{
    volatile Inventory electronInv = new Inventory(InventoryType.Electron);
    volatile Inventory armourInv = new Inventory(InventoryType.Armour);
    volatile Inventory craftingGrid = new Inventory(InventoryType._Crafting);
    @Setter
    Entity entity;
    String username;
    UUID uuid;

    public Player(User user,double movementSpeed,int health, int bDamage) {
        super(user.getName(), movementSpeed, health,20,bDamage);

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
        super("unset",0,20,2,0);
        this.username = "unset";
        this.uuid = UUID.randomUUID();
    }
    public Player(User user,Entity entity){
        super(user.getName(), entity.getMovementSpeed(), entity.getHealth(), entity.getHitboxRadius(), entity.getBDamage());
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
        this.entity = entity;
    }
}
