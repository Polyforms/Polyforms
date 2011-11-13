package org.polyforms.event.bus;

import org.springframework.util.Assert;

/**
 * Base event class.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class Event {
    private final String name;

    /**
     * Create an instance with name.
     */
    public Event(final String name) {
        Assert.hasText(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
