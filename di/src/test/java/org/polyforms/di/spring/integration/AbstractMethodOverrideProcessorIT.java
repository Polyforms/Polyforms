package org.polyforms.di.spring.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public final class AbstractMethodOverrideProcessorIT {
    @Autowired
    private MockInterface mockInterface;

    @Autowired
    private MockObject mockObject;

    @Autowired
    private MockAbstractObject mockAbstractObject;

    @Test
    public void instantiateInterface() {
        Assert.assertNotNull(mockInterface);
    }

    @Test
    public void instantiateAbstractClass() {
        Assert.assertNotNull(mockAbstractObject);
    }

    @Test
    public void instantiateNormalClass() {
        Assert.assertNotNull(mockObject);
    }

    @Test
    public void preDefinedMethodInjection() {
        Assert.assertNotNull(mockAbstractObject.lookup());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invokeUninterceptedMethod() {
        mockInterface.echo("");
    }

    public static abstract class MockAbstractObject {
        public abstract String echo(String string);

        public abstract Object lookup();
    }

    public static interface MockInterface {
        String echo(String string);
    }

    public static final class MockObject {
        public String echo(final String string) {
            return string;
        }
    }
}
