package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.naming.ResourceNamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.Serializable;
@MappedSuperclass
@Configurable
public class Resource implements Serializable {
    @Transient
    @Autowired
    private ResourceNamingService naming;
    @Getter
    @Setter
    public String typeName;
    @Getter
    @Setter
    public String name;
    @Id
    @GeneratedValue
    public Long id;

    public Resource(String name,String typeName){
        this.name = name;
        this.typeName = typeName;
    }
    public Resource(){
        this.typeName = this.getClass().getSimpleName();
    }

    public String toString(){
        return new Gson().toJson(this);
    }

    @EventListener
    public void copyWhenReady(ApplicationReadyEvent e){
        if(this.name == null){
            this.name = this.naming.nameObject(this);
        }
    }
}
