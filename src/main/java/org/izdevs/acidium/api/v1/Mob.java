package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;
import java.util.ArrayList;

public class Mob extends Resource {

    public Mob(String name, ArrayList<SpecObject> objects) {
        super(name, objects);
    }
}
