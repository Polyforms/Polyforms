package org.polyforms.event.integration;

import org.springframework.stereotype.Component;

@Component
public class MockPublisherImpl implements MockPublisher {
    public String subString(final String string, final int start) {
        return string.substring(start);
    }
}
