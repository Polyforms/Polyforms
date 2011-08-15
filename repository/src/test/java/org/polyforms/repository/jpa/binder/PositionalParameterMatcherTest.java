package org.polyforms.repository.jpa.binder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class PositionalParameterMatcherTest {
    @Test
    public void match() {
        final Parameter<?> parameter1 = EasyMock.createMock(Parameter.class);
        final Parameter<?> parameter2 = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new HashSet<Parameter<?>>();
        parameters.add(parameter1);
        parameters.add(parameter2);

        parameter1.getPosition();
        EasyMock.expectLastCall().andReturn(1);
        parameter2.getPosition();
        EasyMock.expectLastCall().andReturn(2);
        EasyMock.replay(parameter1, parameter2);

        final ParameterMatcher<Integer> parameterMatcher = new PositionalParameterMatcher();
        final Map<Integer, Integer> parameterMap = parameterMatcher.match(null, parameters);
        Assert.assertEquals(2, parameterMap.size());
        Assert.assertEquals(0, parameterMap.get(1).intValue());
        Assert.assertEquals(1, parameterMap.get(2).intValue());
        EasyMock.verify(parameter1, parameter2);
    }
}
