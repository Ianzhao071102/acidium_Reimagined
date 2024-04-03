package org.izdevs.acidium.api.v1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.izdevs.acidium.security.RoleManager;

import java.util.HashSet;
import java.util.Set;
@Getter
@Entity
@NoArgsConstructor
public class Role {
    String name;
    Set<Level> levelsGranted = new HashSet<>();
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

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