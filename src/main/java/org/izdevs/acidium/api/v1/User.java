package org.izdevs.acidium.api.v1;

import com.google.re2j.Pattern;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;
import org.springframework.security.core.parameters.P;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;


@Entity
@Getter
@Table(name = "users")
public class User extends Resource {
    @Transient
    public static final Pattern username_regex = Pattern.compile("^[a-zA-Z0-9](?:[._]?[a-zA-Z0-9]){5,17}[a-zA-Z0-9]$");
    @Transient
    public static final Pattern password_regex = Pattern.compile("^[a-zA-Z0-9](?:[._]?[a-zA-Z0-9]){6,30}[a-zA-Z0-9]$");
    String username;
    UUID uuid;
    String passwordHash;

    @ManyToOne(cascade = CascadeType.PERSIST)
    Role role = new Role("Default", Role.Level.PLAYER);

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

    public User(String username, String passwordHash, UUID uuid) {
        this.uuid = uuid;
        this.passwordHash = passwordHash;
        this.username = username;
    }

    public User(String username, String passwordHash, String uuid) {
        this.uuid = UUID.fromString(uuid);
        this.passwordHash = passwordHash;
        this.username = username;
    }
}
