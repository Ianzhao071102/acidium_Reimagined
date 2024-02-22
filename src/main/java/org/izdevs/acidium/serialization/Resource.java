package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.Config;
import org.izdevs.acidium.tick.Ticked;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

import static org.izdevs.acidium.serialization.ResourceFacade.registerResource;

public class Resource implements Ticked {
    @Getter
    @Setter
    Runnable tickRun;
    @Getter
    boolean ticked;
    @Getter
    @Setter
    List<String> flags;
    @Getter
    @Setter
    public boolean unset;
    @Getter
    public boolean isApi;
    @Setter
    public API associatedApi;
    @Setter
    @Getter
    public String typeName;
    @Setter
    @Getter
    public ArrayList<SpecObject> spec;
    public Resource(String name, ArrayList<SpecObject> objects){
        this.typeName = name;
        this.spec = objects;
    }
    public Resource(String name,boolean isApi){
        this.typeName = name;
        this.isApi = isApi;
    }
    public void register(){
        registerResource(this);
    }
    public static Resource deserialize(String serialized){
        Gson gson = new Gson();
        //inline
        return gson.fromJson(serialized,Resource.class);
    }
    public String serialize(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Scheduled(fixedDelay = 1000/Config.ticksPerSecond)
    @Override
    public void tick() {
        if(tickRun != null){
            tickRun.run();
        }
    }
}
