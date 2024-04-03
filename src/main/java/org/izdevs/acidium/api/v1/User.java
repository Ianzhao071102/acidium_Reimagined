package org.izdevs.acidium.api.v1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;

import java.util.UUID;


@Entity
public class User extends Resource {
    String username;
    UUID uuid;
    String passwordHash;
    @Getter
    @Setter
    @OneToOne
    Role role;

    @Getter
    @Id
    @GeneratedValue
    private Long id;

    public User(String username, UUID uuid) {
        super("USER", false);
        this.username = username;
        this.uuid = uuid;
        register();
    }

    public User() {
        super("USER", false);
        this.register();
    }

    public User(String name, String passwordHash) {
        super("USER", false);
        this.passwordHash = passwordHash;
        this.username = name;
    }

}
