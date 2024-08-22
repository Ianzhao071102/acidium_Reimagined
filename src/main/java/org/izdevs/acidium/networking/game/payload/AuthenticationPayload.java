package org.izdevs.acidium.networking.game.payload;

import lombok.Getter;

@Getter
public class AuthenticationPayload {
    String username;
    String passwordHash;
}
