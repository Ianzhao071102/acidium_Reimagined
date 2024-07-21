package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
@MappedSuperclass
public class Resource implements Serializable {
    String typeName;
    String name;
    @Id
    @GeneratedValue
    private Long id;

    public Resource(String name,String typeName){
        this.name = name;
        this.typeName = typeName;
    }
    public Resource(){}

    public String toString(){
        return new Gson().toJson(this);
    }
}
