package org.izdevs.acidium.api.v1;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.game.equipment.DropTable;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;
import org.izdevs.acidium.serialization.models.ResourceSchema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Mob extends Resource {
    String mbName;
    /**
     * no default drop table for entity without this
     */
    @Setter
    DropTable dropTable = null;

    @Setter
    public Set<Equipment> equipments = new HashSet<>();
    @Setter
    double speed;
    @Setter
    int health;
    int hitboxRadius;
    int bDamage; //body damage

    public Mob(String name, double speed, int health, int bDamage, int hitboxRadius) {
       super(name,"Mob");
       this.mbName = name;
       this.speed = speed;
       this.health = health;
       this.bDamage = bDamage;
       this.hitboxRadius = hitboxRadius;
    }
    public String getName(){
        return this.mbName;
    }
}
