package org.izdevs.acidium.networking.game;

import org.hibernate.mapping.Join;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
import org.springframework.stereotype.Service;

@Service
public class PlayerConnectionService {
    //todo FINISH THIS CLASS
    public Player getPlayer(JoinedPlayer player){
        Entity entity
        User user = new User(player.getUsername(),player.getPasswordHash(),player.getUuid());
        Player actual = new Player(user,entity);
        return actual;
    }
    public void attackState(JoinedPlayer player, CombatPositionType position){
       
    }
}
