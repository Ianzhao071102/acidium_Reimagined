package org.izdevs.acidium.api.v1;

import lombok.Getter;
import org.izdevs.acidium.security.RoleManager;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Role {
    String name;
    Set<Level> levelsGranted = new HashSet<>();
    public Role(String name){
        this.name = name;
        levelsGranted.add(Level.PLAYER);
        RoleManager.registerRole(this);
    }
    public Role(String name,Set<Level> levels){
        this.name = name;
        levelsGranted.addAll(levels);
        RoleManager.registerRole(this);
    }

    @Getter
    public enum Level {
        ADMIN(0),
        MODERATOR(1),
        PLAYER(2)
        ;
        final int id;
        Level(int id) {
            this.id = id;
        }
    }
}