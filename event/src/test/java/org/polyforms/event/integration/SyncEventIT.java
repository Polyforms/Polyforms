package org.polyforms.event.integration;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("ComponentScannerIT-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SyncEventIT {
    public static final String BEFORE_EVENT_NAME = "before.string.index";
    public static final String AFTER_EVENT_NAME = "after.string.index";
    @Autowired
    private MockPublisher publisher;
    @Autowired
    private MockSubscriber subscriber;

    @Test
    public void syncEvent() {
        publisher.subString("testString", 4);
        Assert.assertEquals(2, subscriber.getEventCount());
        Assert.assertEquals("testString_4=String", subscriber.getLog());
    }
}
