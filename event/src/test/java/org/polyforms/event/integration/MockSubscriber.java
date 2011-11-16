package org.polyforms.event.integration;

import org.polyforms.event.Subscriber;
import org.springframework.stereotype.Component;

@Component
public class MockSubscriber {
    private int eventCount;
    private String log;

    @Subscriber(SyncEventIT.AFTER_EVENT_NAME)
    public void onEvent(final String string, final String start, final String returnValue) {
        eventCount++;
        log = string + "_" + start + "=" + returnValue;
    }

    @Subscriber(SyncEventIT.BEFORE_EVENT_NAME)
    public void onEvent2() {
        eventCount++;
    }

    public int getEventCount() {
        return eventCount;
    }

    public String getLog() {
        return log;
    }
}
