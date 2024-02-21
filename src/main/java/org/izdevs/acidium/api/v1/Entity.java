package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;

import java.util.ArrayList;

public class Entity extends Resource {
    public Entity(String name) {
        super("Mob",false);
        ArrayList<SpecObject> objects = new ArrayList<>();
        objects.add(new SpecObject("name",name));
        objects.add(new SpecObject("speed",0));
        objects.add(new SpecObject("bDamage",0));
        this.spec = objects;
    }
    public Entity() {
        super("Mob",false);
        ArrayList<SpecObject> objects = new ArrayList<>();
        objects.add(new SpecObject("name","unset"));
        objects.add(new SpecObject("speed",0));
        objects.add(new SpecObject("bDamage",0));
        this.spec = objects;
    }
    public Entity(String name,double speed,int bDamage) {
        super("Mob",false);
        ArrayList<SpecObject> objects = new ArrayList<>();
        objects.add(new SpecObject("name",name));
        objects.add(new SpecObject("speed",speed));
        objects.add(new SpecObject("bDamage",bDamage));
        this.spec = objects;
    }
}
