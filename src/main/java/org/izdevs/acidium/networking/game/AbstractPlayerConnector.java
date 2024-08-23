package org.izdevs.acidium.networking.game;

import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
import org.izdevs.acidium.world.Location;


public interface AbstractPlayerConnector {
    Player getPlayer(JoinedPlayer player);
    void updatePlayer(JoinedPlayer player,Player actual);
    void attackState(JoinedPlayer player,CombatPositionType position);
    void teleport(JoinedPlayer player, Location location);
}
