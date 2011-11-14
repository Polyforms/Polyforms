package org.polyforms.event.bus.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.polyforms.event.bus.Event;
import org.polyforms.event.bus.EventBus;
import org.polyforms.event.bus.Listener;
import org.polyforms.event.bus.ListenerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A simple implementation of {@link EventBus}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
@SuppressWarnings("rawtypes")
public class SimpleEventBus implements EventBus, ListenerRegistry {
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleEventBus.class);
    private final Map<String, Set<Listener>> syncListeners = new ConcurrentHashMap<String, Set<Listener>>();
    private final Map<String, Set<Listener>> asyncListeners = new ConcurrentHashMap<String, Set<Listener>>();

    /**
     * {@inheritDoc}
     */
    public void publish(final Event event) {
        Assert.notNull(event);

        syncPublish(event);
        asyncPublish(event);
    }

    private void syncPublish(final Event event) {
        onEvent(event, syncListeners);
    }

    private void asyncPublish(final Event event) {
        if (asyncListeners.containsKey(event.getName())) {
            throw new UnsupportedOperationException("The async event has not implemented!");
        }
    }

    @SuppressWarnings("unchecked")
    private void onEvent(final Event event, final Map<String, Set<Listener>> subscribers) {
        final String name = event.getName();
        if (subscribers.containsKey(name)) {
            for (final Listener listener : subscribers.get(name)) {
                listener.onEvent(event);
                LOGGER.debug("Publish domain event {} to {}.", name, listener);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void register(final String name, final Listener<?> listener, final boolean async) {
        Assert.hasText(name);
        Assert.notNull(listener);

        register(async ? asyncListeners : syncListeners, name, listener);
    }

    private void register(final Map<String, Set<Listener>> listeners, final String name, final Listener<?> listener) {
        if (!listeners.containsKey(name)) {
            listeners.put(name, new HashSet<Listener>());
        }
        listeners.get(name).add(listener);
        LOGGER.info("Register {} to {}.", listener, name);
    }

    /**
     * {@inheritDoc}
     */
    public void unregister(final String name, final Listener<?> listener) {
        Assert.hasText(name);
        Assert.notNull(listener);

        syncListeners.get(name).remove(listener);
        LOGGER.info("Unregister {} from {}.", listener, name);
    }
}
