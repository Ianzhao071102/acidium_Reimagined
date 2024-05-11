package org.izdevs.acidium.api.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;

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
    Role role = new Role("Default");

    @Getter
    @Id
    @GeneratedValue
    private Long id;

    public User(String username, UUID uuid) {
        super(username, false);
        this.username = username;
        this.uuid = uuid;
        register();
    }

    public User() {
        super("USER", false);
        this.uuid = UUID.randomUUID();
        this.register();
    }

    public User(String name, String passwordHash) {
        super(name,false);
        this.uuid = UUID.randomUUID();
        this.passwordHash = passwordHash;
        this.username = name;
    }

    public User(String username,String passwordHash,UUID uuid){
        super(username,false);
        this.uuid = uuid;
        this.passwordHash = passwordHash;
        this.username = username;
    }
    public User(String username,String passwordHash,String uuid){
        super(username,false);
        this.uuid = UUID.fromString(uuid);
        this.passwordHash = passwordHash;
        this.username = username;
    }
}
