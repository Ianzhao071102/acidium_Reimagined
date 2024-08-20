package org.izdevs.acidium.api.v1;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Role {
    String name;

    Level levelGranted = Level.PLAYER;

    Level level;
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, Level level) {
        this.name = name;
        this.levelGranted = level;
    }
    public Role(){}

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