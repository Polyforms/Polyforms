package org.polyforms.event.bus.support;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.event.bus.Event;
import org.polyforms.event.bus.Listener;

public class SimpleEventBusTest {
    private SimpleEventBus eventBus;
    @SuppressWarnings("rawtypes")
    private Listener listener;

    @Before
    public void setUp() {
        eventBus = new SimpleEventBus();
        listener = EasyMock.createMock(Listener.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void syncPublish() {
        final Event event = new Event("sync");
        listener.onEvent(event);
        EasyMock.replay(listener);

        eventBus.register("sync", listener, false);
        // Just for testing cache
        eventBus.register("sync", listener, false);
        eventBus.publish(event);
        EasyMock.verify(listener);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void asyncPublish() {
        eventBus.register("sync", listener, true);
        eventBus.publish(new Event("sync"));
    }

    @Test
    public void unregister() {
        eventBus.register("sync", listener, false);
        eventBus.unregister("sync", listener);
        eventBus.publish(new Event("sync"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void publishNull() {
        eventBus.publish(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerWithNullName() {
        eventBus.register(null, EasyMock.createMock(Listener.class), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerWithBlankName() {
        eventBus.register(" ", EasyMock.createMock(Listener.class), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerWithNullListener() {
        eventBus.register("sync", null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unregisterWithNullName() {
        eventBus.unregister(null, EasyMock.createMock(Listener.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unregisterWithBlankName() {
        eventBus.unregister(" ", EasyMock.createMock(Listener.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unregisterWithNullListener() {
        eventBus.unregister("sync", null);
    }
}
