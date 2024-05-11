package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;

import java.util.ArrayList;
import java.util.List;

import static org.izdevs.acidium.serialization.ResourceFacade.registerResource;

public class Resource {
    @Getter
    @Setter
    @Expose(serialize = false, deserialize = false)
    public boolean unset;
    @Getter
    @Expose(serialize = false, deserialize = false)
    public boolean isApi;
    @Setter
    @Expose(serialize = false, deserialize = false)

    public API associatedApi;
    @Setter
    @Getter
    @Deprecated
    @Expose(serialize = false, deserialize = false)

    public String typeName;
    @Getter
    @Setter
    @Expose(serialize = false, deserialize = false)

    public String name;
    @Setter
    @Getter
    @Expose(serialize = false, deserialize = false)

    public ArrayList<SpecObject> spec;
    protected CompoundTag data;
    @Expose(serialize = false, deserialize = false)
    @Getter
    @Setter

    Runnable tickRun = () -> {

    };
    @Getter
    @Expose(serialize = false, deserialize = false)
    boolean ticked = false;
    @Getter
    @Setter
    @Expose(serialize = false, deserialize = false)
    List<String> flags;

    public Resource(String name, ArrayList<SpecObject> objects) {
        this.name = name;
        this.spec = objects;
        LoopManager.registerRepeatingTask(new ScheduledTask(tickRun));
    }

    public Resource(String name, boolean isApi) {
        this.name = name;
        this.isApi = isApi;
        LoopManager.registerRepeatingTask(new ScheduledTask(tickRun));
    }

    public void register() {
        registerResource(this);
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Deprecated
    /**
     * bro fuck do not use this, this is dangerous this is only used to help lombok
     */
    public Resource(){
        this.unset = true;
    }
}
