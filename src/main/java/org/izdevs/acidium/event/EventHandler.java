package org.izdevs.acidium.event;

import lombok.Getter;

@Getter
public abstract class EventHandler{
   abstract boolean onEventFire();
   abstract boolean onEventCancel();
   EventHandlerPriorities priority = EventHandlerPriorities.DEFAULT;
   Event associated;

}
