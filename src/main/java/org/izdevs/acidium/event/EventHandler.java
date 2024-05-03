package org.izdevs.acidium.event;

import lombok.Getter;

@Getter
public abstract class EventHandler{
   abstract void onEventFire();
   abstract void onEventCancel();
   EventHandlerPriorities priority = EventHandlerPriorities.DEFAULT;
   Event associated;

}
