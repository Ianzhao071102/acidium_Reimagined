package org.izdevs.acidium.account;

import org.apache.kafka.common.resource.Resource;
import org.apache.kafka.common.resource.ResourceType;

import java.util.UUID;

public class User extends Resource {
    UUID playerId;
    UUID session;
    public User(ResourceType resourceType, String name,UUID playerId,UUID session) {
        super(resourceType, name);
        this.playerId = playerId;
        this.session = session;
    }
}
