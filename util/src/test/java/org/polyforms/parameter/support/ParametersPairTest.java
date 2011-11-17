package org.polyforms.parameter.support;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.parameter.Parameters;
import org.polyforms.parameter.support.MethodParameterMatcher.ParametersPair;

public class ParametersPairTest {
    private ParametersPair parametersPairA;
    private ParametersPair parametersPairB;
    private Parameters<?> parameterA;
    private Parameters<?> parameterB;

    @Before
    public void setUp() {
        parameterA = EasyMock.createMock(Parameters.class);
        parameterB = EasyMock.createMock(Parameters.class);
        parametersPairA = new ParametersPair(parameterA, parameterB);
        parametersPairB = new ParametersPair(EasyMock.createMock(Parameters.class),
                EasyMock.createMock(Parameters.class));
    }

    @Test
    public void hashcode() {
        Assert.assertTrue(parametersPairA.hashCode() != parametersPairB.hashCode());
    }

    @Test
    public void equalsSame() {
        Assert.assertTrue(parametersPairA.equals(parametersPairA));
    }

    @Test
    public void notEqualsNull() {
        Assert.assertFalse(parametersPairA.equals(null));
    }

    @Test
    public void notEqualsOtherClass() {
        Assert.assertFalse(parametersPairA.equals(new Object()));
    }

    @Test
    public void notEqualsSource() throws NoSuchMethodException {
        Assert.assertFalse(parametersPairA.equals(new ParametersPair(EasyMock.createMock(Parameters.class), parameterB)));
    }

    @Test
    public void notEqualsTarget() {
        Assert.assertFalse(parametersPairA.equals(new ParametersPair(parameterA, EasyMock.createMock(Parameters.class))));
    }

    @Test
    public void equals() throws NoSuchMethodException {
        Assert.assertTrue(parametersPairA.equals(new ParametersPair(parameterA, parameterB)));
    }
}
