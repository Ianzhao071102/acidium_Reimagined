package org.izdevs.acidium.api.v1;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.izdevs.acidium.security.RoleManager;
import org.izdevs.acidium.utils.SpringBeanUtils;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Role {
    String name;

    @OneToMany
    Set<Level> levelsGranted = new HashSet<>();
    @Lob
    Level level;
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    public Role(String name) {
        this.name = name;
        levelsGranted.add(Level.PLAYER);
        RoleManager manager = (RoleManager) SpringBeanUtils.getBean("roleManager");
        manager.registerRole(this);
    }

    public Role(String name, Set<Level> levels) {
        this.name = name;
        levelsGranted.addAll(levels);
        RoleManager manager = (RoleManager) SpringBeanUtils.getBean("roleManager");
        manager.registerRole(this);
    }


    public enum Level {
        ADMIN(0), MODERATOR(1), PLAYER(2);
        final int id;

        Level(int id) {
            this.id = id;
        }
    }
}