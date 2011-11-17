package org.polyforms.parameter.support;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.Parameters;

public class SourceParametersTest {
    private SourceParameters sourceParameters;
    private Parameters<?> parameters;
    private final Parameter parameter1 = createParameter(String.class, "name", 0);
    private final Parameter parameter2 = createParameter(Integer.class, "index", 1);
    private final Parameter parameter3 = createParameter(Integer.class, null, 2);

    @Before
    public void setUp() {
        parameters = EasyMock.createMock(Parameters.class);
        sourceParameters = new SourceParameters(parameters);
    }

    private void mockParameters(final int times) {
        parameters.getParameters();
        EasyMock.expectLastCall().andReturn(new Parameter[] { parameter1, parameter2, parameter3 }).times(times);
        EasyMock.replay(parameters);
    }

    private Parameter createParameter(final Class<?> type, final String name, final int index) {
        final Parameter parameter = new Parameter();
        parameter.setType(type);
        parameter.setName(name);
        parameter.setIndex(index);
        return parameter;
    }

    @Test
    public void matchByName() {
        mockParameters(1);
        Assert.assertEquals(parameter2, sourceParameters.match(createParameter(String.class, "index", 0)));

        // Just for testing cache
        Assert.assertEquals(parameter2, sourceParameters.match(createParameter(String.class, "index", 0)));
    }

    @Test
    public void matchByType() {
        mockParameters(2);
        Assert.assertEquals(parameter1, sourceParameters.match(createParameter(String.class, null, 1)));

        // Just for testing cache
        Assert.assertEquals(parameter1, sourceParameters.match(createParameter(String.class, null, 1)));
    }

    @Test
    public void matchByIndex() {
        mockParameters(3);
        Assert.assertEquals(parameter2, sourceParameters.match(createParameter(Float.class, null, 1)));

        // Just for testing cache
        Assert.assertEquals(parameter2, sourceParameters.match(createParameter(Float.class, null, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unmatch() {
        mockParameters(3);
        sourceParameters.match(createParameter(Integer.class, null, 4));
    }

    @After
    public void tearDown() {
        EasyMock.verify(parameters);
    }
}
