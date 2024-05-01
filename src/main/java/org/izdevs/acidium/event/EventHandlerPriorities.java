package org.izdevs.acidium.event;

public enum EventHandlerPriorities {
    LOWEST(-2),
    LOW(-1),
    DEFAULT(0),
    HIGH(1),
    HIGHEST(2);

    final int id;
    EventHandlerPriorities(int i) {
        this.id = i;
    }
}
