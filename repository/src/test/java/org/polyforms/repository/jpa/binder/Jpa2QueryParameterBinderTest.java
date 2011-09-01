package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Parameter;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.springframework.test.util.ReflectionTestUtils;

public class Jpa2QueryParameterBinderTest {
    private QueryParameterBinder queryParameterBinder;

    @Before
    public void setUp() {
        queryParameterBinder = new Jpa2QueryParameterBinder();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void bindPositionalParamaters() throws NoSuchMethodException {
        final ParameterBinder<Integer> positionalParameterBinder = EasyMock.createMock(ParameterBinder.class);
        ReflectionTestUtils.setField(queryParameterBinder, "positionalParameterBinder", positionalParameterBinder);

        final Method method = String.class.getMethod("startsWith", new Class<?>[] { String.class });
        final Object[] arguments = new Object[] { "tony" };
        final Query query = EasyMock.createMock(Query.class);
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new HashSet<Parameter<?>>();
        parameters.add(parameter);

        query.getParameters();
        EasyMock.expectLastCall().andReturn(parameters);
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(1);
        positionalParameterBinder.bind(query, method, arguments);
        EasyMock.replay(query, parameter, positionalParameterBinder);

        queryParameterBinder.bind(query, method, arguments);
        EasyMock.verify(query, parameter, positionalParameterBinder);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void bindNamedParameters() throws NoSuchMethodException {
        final ParameterBinder<String> namedParameterBinder = EasyMock.createMock(ParameterBinder.class);
        ReflectionTestUtils.setField(queryParameterBinder, "namedParameterBinder", namedParameterBinder);

        final Method method = String.class.getMethod("startsWith", new Class<?>[] { String.class });
        final Object[] arguments = new Object[] { "tony" };
        final Query query = EasyMock.createMock(Query.class);
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new HashSet<Parameter<?>>();
        parameters.add(parameter);

        query.getParameters();
        EasyMock.expectLastCall().andReturn(parameters);
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(null);
        namedParameterBinder.bind(query, method, arguments);
        EasyMock.replay(query, parameter, namedParameterBinder);

        queryParameterBinder.bind(query, method, arguments);
        EasyMock.verify(query, parameter, namedParameterBinder);
    }

    @Test
    public void noArgumentsToBind() throws NoSuchMethodException {
        final Method method = String.class.getMethod("startsWith", new Class<?>[] { String.class });
        final Query query = EasyMock.createMock(Query.class);

        query.getParameters();
        EasyMock.expectLastCall().andReturn(Collections.EMPTY_SET);
        EasyMock.replay(query);

        queryParameterBinder.bind(query, method, new Object[0]);
        EasyMock.verify(query);
    }
}
