package org.polyforms.event.bus;

import junit.framework.Assert;

import org.junit.Test;

public class EventTest {
    @Test
    public void newInstance() {
        final Event event = new Event("name");
        Assert.assertEquals("name", event.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void newInstanceWithNull() {
        new Event(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newInstanceWithBlank() {
        new Event(" ");
    }
}
