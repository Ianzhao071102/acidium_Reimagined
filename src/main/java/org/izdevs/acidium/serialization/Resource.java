package org.izdevs.acidium.serialization;

import com.google.gson.Gson;

import java.io.Serializable;

public class Resource implements Serializable {
    String name;
    public Resource(String name){
        this.name = name;
    }
    public Resource(){}

    public String toString(){
        return new Gson().toJson(this);
    }
}
