package org.polyforms.event.integration;

import org.polyforms.event.Publisher;
import org.polyforms.event.Publishers;

public interface MockPublisher {
    @Publishers({ @Publisher(SyncEventIT.BEFORE_EVENT_NAME), @Publisher(SyncEventIT.AFTER_EVENT_NAME) })
    String subString(String string, int start);
}
