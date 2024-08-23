package org.izdevs.acidium.networking.game;

import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
import org.izdevs.acidium.world.Location;
import org.springframework.stereotype.Service;


@Service
public class PlayerConnectionService implements AbstractPlayerConnector{

    @Override
    public Player getPlayer(JoinedPlayer player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayer'");
    }


    @Override
    public void attackState(JoinedPlayer player, CombatPositionType position) {
        Player ap = this.getPlayer(player);
        ap.setPosition(position);
        this.updatePlayer(player, ap);
    }

    @Override
    public void teleport(JoinedPlayer player, Location location) {
        Player ap = this.getPlayer(player);
        ap.setX(location.getX());
        ap.setY(location.getY());
        this.updatePlayer(player,ap);
    }


    @Override
    public void updatePlayer(JoinedPlayer player, Player actual) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePlayer'");
    }

}
