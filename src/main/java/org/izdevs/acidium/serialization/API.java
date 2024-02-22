package org.izdevs.acidium.serialization;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

import static org.izdevs.acidium.serialization.ResourceFacade.registerAPI;

public class API extends Resource{

    @Getter
    public static int zero = 0;
    @Getter
    String pointer; //the pointer to it, like: v1 or beta.acidium.izdevs.org/v1beta1sub1

    @Getter
    ArrayList<org.izdevs.acidium.serialization.Resource> resources;

    public API(String name, String pointer, Resource... resources) {
        super(name,true);
        this.pointer = pointer;
        this.resources = new ArrayList<>(Arrays.asList(resources).subList(0, resources.length + 1));
        registerAPI(this);
    }

    public API(String name){
        super(name,true);
        registerAPI(this);
    }
}
