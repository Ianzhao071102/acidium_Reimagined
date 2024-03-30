package org.izdevs.acidium.security;

import lombok.Getter;
import org.izdevs.acidium.api.v1.Payload;

@Getter
public class AuthorizationContent extends Payload {
    String uuid;
    String sessionData;
    public AuthorizationContent(String uuid,String sessionData){
        this.uuid = uuid;
        this.sessionData = sessionData;
    }
}
