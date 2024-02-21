package org.izdevs.acidium.serialization;

import lombok.Getter;
import org.izdevs.acidium.tick.Ticked;

import java.util.ArrayList;


public class ResourceFacade implements Ticked {
    @Getter
    static ArrayList<Resource> resources = new ArrayList<>();

    @Override
    public void tick() {

    }
    public static void registerAPI(API api){

    }
    public static void registerResource(Resource resource) {
        resources.add(resource);
    }

}
