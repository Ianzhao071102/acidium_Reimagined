package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
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
    @Expose(serialize = false,deserialize = false)
    @Getter
    @Setter
    Runnable tickRun;
    @Getter
    @Expose(serialize = false,deserialize = false)
    boolean ticked;
    @Getter
    @Setter
    @Expose(serialize = false,deserialize = false)
    List<String> flags;
    @Getter
    @Setter
    @Expose(serialize = false,deserialize = false)
    public boolean unset;
    @Getter
    @Expose(serialize = false,deserialize = false)
    public boolean isApi;
    @Setter
    @Expose(serialize = false,deserialize = false)

    public API associatedApi;
    @Setter
    @Getter
    @Deprecated
    @Expose(serialize = false,deserialize = false)

    public String typeName;
    @Getter
    @Setter
    @Expose(serialize = false,deserialize = false)

    public String name;
    @Setter
    @Getter
    @Expose(serialize = false,deserialize = false)

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
