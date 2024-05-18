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
    protected CompoundTag data;

    public Resource(String name, ArrayList<SpecObject> objects) {
        this.name = name;
        this.spec = objects;
    }

    public Resource(String name, boolean isApi) {
        this.name = name;
        this.isApi = isApi;
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
