package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;

import java.util.ArrayList;
import java.util.UUID;

public class User extends Resource {

    ArrayList<SpecObject> specof = new ArrayList<>();
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
        register();
    }
}
