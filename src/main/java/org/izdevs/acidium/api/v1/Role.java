package org.izdevs.acidium.api.v1;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Role {
    String name;

    @ElementCollection(targetClass = Level.class)
    @Column(name = "levels_granted",nullable = false)
    @Enumerated(EnumType.STRING)
    Set<Level> levelsGranted = new HashSet<>();

    Level level;
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    public Role(String name) {
        this.name = name;
        levelsGranted.add(Level.PLAYER);
    }

    public Role(String name, Set<Level> levels) {
        this.name = name;
        levelsGranted.addAll(levels);
    }


    public enum Level {
        ADMIN(0), MODERATOR(1), PLAYER(2);
        @Id
        int id = 2;

        Level(int id) {
            this.id = id;
        }

        Level() {
            for(int i=0;i<=Level.values().length-1;i++){
                if(Level.values()[i].equals(this)) {
                    id = i;
                    break;
                }
            }
        }
    }
}