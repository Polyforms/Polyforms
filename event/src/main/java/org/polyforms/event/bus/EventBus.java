package org.polyforms.event.bus;

/**
 * Event bus used to publish an event. It distributes events based on the name of event.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface EventBus {
    /**
     * Publish an event.
     */
    void publish(Event event);
}
