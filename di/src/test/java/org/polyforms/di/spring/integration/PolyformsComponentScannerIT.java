package org.polyforms.di.spring.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("ComponentScannerIT-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PolyformsComponentScannerIT {
    @Autowired
    private MockInterface mockInterface;

    @Test
    public void instantiateInterface() {
        Assert.assertNotNull(mockInterface);
    }

    @Component
    public static interface MockInterface {
        String echo(String string);
    }
}
