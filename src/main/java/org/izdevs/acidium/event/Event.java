package org.izdevs.acidium.event;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;

import java.util.*;

public abstract class Event {
    @Getter
    public final boolean async;
    public String name;
    @Getter
    @Setter
    boolean cancelled = false;
    Comparator<EventHandler> compare_based_on_priority = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            assert o1 instanceof EventHandler && o2 instanceof EventHandler;
            EventHandler o3 = (EventHandler) o1;
            EventHandler o4 = (EventHandler) o2;
            return o3.getPriority().id - o4.getPriority().id;
        }
    };

    public Event(String name, boolean isAsync) {
        this.async = isAsync;
    }

    /**
     *
     * @return fires All the shit at loop manager to let it call the handlers that the event has been cancelled
     */
    public boolean cancel() {
        this.cancelled = true;
        this.getHandlers().sort(compare_based_on_priority);
        LoopManager.scheduleAsyncDelayedTask(new DelayedTask(() -> {
            for(EventHandler handler:this.getHandlers()){
                handler.onEventCancel();
            }
        },0L));
        return true;
    }

    abstract List<EventHandler> getHandlers();

    /**
     * if the name is not specified, return the class name
     *
     * @return the name of the event
     */
    public String getName() {
        return Objects.requireNonNullElseGet(name, () -> this.getClass().getSimpleName());
    }

    public enum Result {
        /**
         * The server denied the event, the server might have cancelled the event.
         * The event will either not take place or being reverted based on what the event is.
         */
        DENY,
        /**
         * Continue processing until either DENY or ALLOW
         */
        DEFAULT,
        /**
         * The action is forced or allowed by the server, it will take place if possible
         * some might not be taken place.
         */
        ALLOW
    }
}
