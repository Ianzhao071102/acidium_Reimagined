package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.beans.factory.annotation.Autowired;


public class Resource {
    @Autowired
    ResourceFacade facade;

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
    @Id
    @GeneratedValue
    private Long id;

    public Resource(String name, boolean isApi) {
        this.name = name;
        this.isApi = isApi;
    }
    public void register() {
        facade.registerResource(this);
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    @Getter
    ResourceSchema schema;
}
