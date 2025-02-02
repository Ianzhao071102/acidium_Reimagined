package org.izdevs.acidium.networking.game;

import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.game.entity.mechanics.PlayerEntity;
import org.izdevs.acidium.game.player.PlayerHolder;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
import org.izdevs.acidium.world.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PlayerConnectionService implements AbstractPlayerConnector {
    @Autowired
    PlayerHolder holder;

    @Override
    public PlayerEntity getPlayer(JoinedPlayer player) {
        for (PlayerEntity p : holder.players) {
            if (p.getUsername().equals(player.getUsername())) {
                return p;
            }
        }
        throw new IllegalArgumentException("player with username:" + player.getUsername() + " is NOT found");
    }


    @Override
    public void attackState(JoinedPlayer player, CombatPositionType position) {
        PlayerEntity ap = this.getPlayer(player);
        ap.setPosition(position);
        this.updatePlayer(player, ap);
    }

    @Override
    public void teleport(JoinedPlayer player, Location location) {
        PlayerEntity ap = this.getPlayer(player);
        ap.setX(location.getX());
        ap.setY(location.getY());
        this.updatePlayer(player, ap);
    }


    @Override
    public void updatePlayer(JoinedPlayer player, PlayerEntity actual) {
        PlayerEntity ap = this.getPlayer(player);
        holder.players.remove(ap);

        holder.players.add(actual);
    }

}
