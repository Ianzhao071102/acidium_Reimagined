package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.Resource;
import org.springframework.stereotype.Component;

@Component
public class CommandDefinition extends Resource {
    String command;
    public CommandDefinition() {

    }
    public CommandDefinition(String command) {
        this.command = command;
    }
}
