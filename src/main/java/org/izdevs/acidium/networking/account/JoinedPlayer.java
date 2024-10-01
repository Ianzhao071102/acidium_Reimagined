package org.izdevs.acidium.networking.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.izdevs.acidium.networking.game.payload.CombatPositionType;

@Getter
@Setter
@Entity
public class JoinedPlayer {
    String username;
    String passwordHash;
    String uuid;
    @Id
    @GeneratedValue
    private Long id;
    String world_name;

    public JoinedPlayer() {}
    public JoinedPlayer(String username,String passwordHash,String uuid){
        this.username = username;
        this.passwordHash = passwordHash;
        this.uuid = uuid;
    }
}
