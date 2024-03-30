package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;

import java.util.ArrayList;
import java.util.UUID;

public class User extends Resource {
    @Getter
    @Setter
    Role role;
    ArrayList<SpecObject> specof = new ArrayList<>();
    @Getter
    @Setter
    String passwordHash = "";
    public User(String username, UUID uuid) {
        super("USER", false);
        SpecObject usernameSpec = new SpecObject("username",username);
        specof.add(usernameSpec);

        SpecObject uuidSpec = new SpecObject("uuid",uuid.toString());
        specof.add(uuidSpec);
        this.spec = specof;
        register();
    }
    public User(){
        super("USER", false);
        SpecObject usernameSpec = new SpecObject("username","unset");
        specof.add(usernameSpec);

        SpecObject uuidSpec = new SpecObject("uuid","unset");
        specof.add(uuidSpec);
        this.spec = specof;
        this.register();
    }
    public User(String name,String passwordHash){
        super("USER",false);
        SpecObject username = new SpecObject("username",name);
        SpecObject uuid = new SpecObject("uuid",UUID.randomUUID().toString());
        specof.add(username);
        specof.add(uuid);
        this.setSpec(specof);
        this.passwordHash = passwordHash;
    }
}
