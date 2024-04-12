package org.izdevs.acidium.api.v1;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Mob extends Resource {
    @Setter
    public Set<Equipment> equipments = new HashSet<>();

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id

    int id;
    @Setter
    int x, y;
    @Setter
    double speed;
    @Setter
    int health;
    int hitboxRadius;
    int bDamage; //body damage

    public Mob(String name, double speed, int health, int bDamage, int hitboxRadius) {
        super("Mob", false);
        ArrayList<SpecObject> objects = new ArrayList<>();
        objects.add(new SpecObject("name", name));
        objects.add(new SpecObject("speed", speed));
        objects.add(new SpecObject("bDamage", bDamage));
        objects.add(new SpecObject("health", health));
        objects.add(new SpecObject("hitboxRadius", hitboxRadius));
        this.spec = objects;
    }

    public Mob() {
        super("Mob", false);
        ArrayList<SpecObject> objects = new ArrayList<>();
        objects.add(new SpecObject("name", "unset"));
        objects.add(new SpecObject("speed", 0));
        objects.add(new SpecObject("bDamage", 0));
        this.spec = objects;
    }
}
