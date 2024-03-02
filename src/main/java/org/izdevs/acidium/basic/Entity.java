package org.izdevs.acidium.basic;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Entity {
    double x,y;
    String name;
    double movementSpeed;
    int health;
    int hitboxRadius;
    int bDamage;
    public Entity(String name,double movementSpeed,int health,int hitboxRadius,int bDamage){
        this.name = name;
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.hitboxRadius = hitboxRadius;
        this.bDamage = bDamage;
    }
}
