package org.polyforms.event.bus;

/**
 * Event listener.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface Listener<E extends Event> {
    /**
     * Action when an event existed.
     */
    void onEvent(E event);
}
