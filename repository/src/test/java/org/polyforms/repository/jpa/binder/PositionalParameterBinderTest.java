package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Parameter;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class PositionalParameterBinderTest {
    private PositionalParameterBinder parameterBinder;

    @Before
    public void setUp() {
        parameterBinder = new PositionalParameterBinder();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void parameterMatchers() {
        final List<ParameterMatcher<Integer>> parameterMatchers = (List<ParameterMatcher<Integer>>) ReflectionTestUtils
                .getField(parameterBinder, "parameterMatchers");
        Assert.assertEquals(2, parameterMatchers.size());
        Assert.assertTrue(parameterMatchers.get(0) instanceof TypedParameterMatcher);
        Assert.assertTrue(parameterMatchers.get(1) instanceof PositionalParameterMatcher);
    }

    @Test
    public void bind() throws NoSuchMethodException {
        final Method method = String.class.getMethod("startsWith", new Class<?>[] { String.class });
        final Query query = EasyMock.createMock(Query.class);
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new HashSet<Parameter<?>>();
        parameters.add(parameter);

        parameter.getParameterType();
        EasyMock.expectLastCall().andReturn(Object.class);
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(1);
        query.setParameter(1, "value");
        EasyMock.expectLastCall().andReturn(query);
        query.setParameter(1, "code");
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(query, parameter);

        parameterBinder.bind(query, method, parameters, new Object[] { "value" });
        // Just for testing cache
        parameterBinder.bind(query, method, parameters, new Object[] { "code" });
        EasyMock.verify(query, parameter);
    }

    @Test
    public void setParameter() {
        final Query query = EasyMock.createMock(Query.class);
        query.setParameter(1, "value");
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(query);

        parameterBinder.setParameter(query, 1, "value");
        EasyMock.verify(query);

    }

    @Test
    public void getKey() {
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(1);
        EasyMock.replay(parameter);

        Assert.assertEquals(1, parameterBinder.getKey(parameter).intValue());
        EasyMock.verify(parameter);
    }
}
