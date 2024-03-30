package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.configuration.Config;
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
    @Deprecated
    public String typeName;
    @Getter
    @Setter
    public String name;
    @Setter
    @Getter
    public ArrayList<SpecObject> spec;
    public Resource(String name, ArrayList<SpecObject> objects){
        this.name = name;
        this.spec = objects;
    }
    public Resource(String name,boolean isApi){
        this.name = name;
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
    //IDEA: THIS COULD BE SERIALIZED TO NBT IF COULD
    @Scheduled(fixedDelay = 1000/Config.ticksPerSecond)
    @Override
    public void tick() {
        if(tickRun != null){
            tickRun.run();
        }
    }
    protected CompoundTag data;
}
