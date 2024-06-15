package org.izdevs.acidium.serialization;

import lombok.Getter;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;



public class API extends Resource{
    @Autowired
    ResourceFacade facade;

    @Getter
    public static int zero = 0;
    @Getter
    String pointer; //the pointer to it, like: v1 or beta.acidium.izdevs.org/v1beta1sub1

    @Getter
    ArrayList<org.izdevs.acidium.serialization.Resource> resources;

    public API(String name, String pointer, Resource... resources) {
        super(name,true);
        this.pointer = pointer;
        this.resources = new ArrayList<>(Arrays.asList(resources).subList(0, resources.length));
        facade.registerAPI(this);
    }

    public API(String name){
        super(name,true);
        facade.registerAPI(this);
    }

    @Getter
    ResourceSchema schema;
}
