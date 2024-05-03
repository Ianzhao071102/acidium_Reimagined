package org.izdevs.acidium.security;

import lombok.Getter;
import org.izdevs.acidium.api.v1.Payload;

@Getter
public class AuthorizationContent {
    String uuid;
    SessionDetail sessionData;
    public AuthorizationContent(String uuid, SessionDetail sessionData){
        this.uuid = uuid;
        this.sessionData = sessionData;
    }
}
