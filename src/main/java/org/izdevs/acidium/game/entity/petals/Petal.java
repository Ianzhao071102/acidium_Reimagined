package org.izdevs.acidium.game.entity.petals;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.World;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter

public class Petal extends Resource {
    @Transient
    Map<String,Object> attribute = new HashMap<>();
    double movementSpeed;
    int health;
    int hitboxRadius;
    int bDamage;
    @Id
    @GeneratedValue
    private Long id;

    public Petal() {

    }
    public Petal(double movementSpeed,int health,int hitboxRadius,int bDamage){
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.hitboxRadius = hitboxRadius;
        this.bDamage = bDamage;
    }
}
