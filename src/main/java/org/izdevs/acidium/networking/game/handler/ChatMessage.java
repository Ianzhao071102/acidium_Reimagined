package org.izdevs.acidium.networking.game.handler;

import org.izdevs.acidium.api.v1.User;

public class ChatMessage {
    public User sender;
    public enum TYPE{
        WORLD,
        DIRECT,
        PARTY
    }
    public TYPE type;
    public String message;

    /**
     * WORLD: world  fails if player is not in the world
     * DIRECT: direct-<player-id>
     * PARTY: party-<party-id> fails if the player is not in the party
     */
    public String receiver_id;
}
