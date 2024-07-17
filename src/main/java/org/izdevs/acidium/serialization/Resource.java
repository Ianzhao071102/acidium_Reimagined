package org.izdevs.acidium.serialization;

import com.google.gson.Gson;

import java.io.Serializable;

public class Resource implements Serializable {


    public String toString(){
        return new Gson().toJson(this);
    }
}
