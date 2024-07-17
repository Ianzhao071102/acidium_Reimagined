package org.izdevs.acidium.api.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;


@Entity
@Table(name = "users")
@Getter

public class User extends Resource {
    String username;
    UUID uuid;
    String passwordHash;
    @Getter
    @Setter
    @OneToOne
    Role role = new Role("Default", Collections.singleton(Role.Level.PLAYER));

    @Getter
    @Id
    @GeneratedValue
    private Long id;

    public User(String username, UUID uuid) {
        this.username = username;
        this.uuid = uuid;
    }

    public User() {
        this.uuid = UUID.randomUUID();
    }

    public User(String name, String passwordHash) {
        this.uuid = UUID.randomUUID();
        this.passwordHash = passwordHash;
        this.username = name;
    }

    public User(String username,String passwordHash,UUID uuid){
        this.uuid = uuid;
        this.passwordHash = passwordHash;
        this.username = username;
    }
    public User(String username,String passwordHash,String uuid){
        this.uuid = UUID.fromString(uuid);
        this.passwordHash = passwordHash;
        this.username = username;
    }
}
