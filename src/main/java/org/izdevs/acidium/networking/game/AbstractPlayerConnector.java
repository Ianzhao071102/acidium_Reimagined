package org.izdevs.acidium.networking.game;

import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.game.entity.mechanics.PlayerEntity;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
import org.izdevs.acidium.world.Location;


public interface AbstractPlayerConnector {
    PlayerEntity getPlayer(JoinedPlayer player);
    void updatePlayer(JoinedPlayer player,PlayerEntity p);
    void attackState(JoinedPlayer player,CombatPositionType position);
    void teleport(JoinedPlayer player, Location location);
}
