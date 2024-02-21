package org.izdevs.acidium.api.v1;

import org.izdevs.acidium.serialization.Resource;

public class CommandDefinition extends Resource {
    public CommandDefinition() {
        super("CommandDefinition",false);
    }
    public CommandDefinition(String command) {
        super("CommandDefinition" + command,false);
    }
}
