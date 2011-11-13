package org.polyforms.event.bus;

/**
 * Registry used to hold all event listeners.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ListenerRegistry {
    /**
     * Register a listener by name.
     */
    void register(String name, Listener<?> listener, boolean async);

    /**
     * Unregister a listener by name.
     */
    void unregister(String name, Listener<?> listener);
}
