package org.izdevs.acidium.networking.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;

@Getter
@Entity
public class JoinedPlayer {
    String username;
    String passwordHash;
    String uuid;
    CombatPositionType position = CombatPositionType.NORMAL;
    @Id
    @GeneratedValue
    private Long id;

    public JoinedPlayer() {}
    public JoinedPlayer(String username,String passwordHash,String uuid){
        this.username = username;
        this.passwordHash = passwordHash;
        this.uuid = uuid;
    }
}
