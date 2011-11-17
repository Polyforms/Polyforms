package org.polyforms.repository.jpa.support;

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

public class Jpa2QueryParameterBinderTest {
    private QueryParameterBinder queryParameterBinder;

    @Before
    public void setUp() {
        queryParameterBinder = new Jpa2QueryParameterBinder();
    }

    @Test
    public void bindPositionalParamaters() throws NoSuchMethodException {
        final Method method = String.class.getMethod("startsWith", new Class<?>[] { String.class });
        final Object[] arguments = new Object[] { "tony" };
        final Query query = EasyMock.createMock(Query.class);
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new HashSet<Parameter<?>>();
        parameters.add(parameter);

        query.getParameters();
        EasyMock.expectLastCall().andReturn(parameters).times(2);
        parameter.getParameterType();
        EasyMock.expectLastCall().andReturn(String.class);
        parameter.getName();
        EasyMock.expectLastCall().andReturn(null);
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(1).times(6);
        query.setParameter(1, "tony");
        EasyMock.expectLastCall().andReturn(query).times(2);
        EasyMock.replay(query, parameter);

        queryParameterBinder.bind(query, method, arguments);
        // Just for testing cache
        queryParameterBinder.bind(query, method, arguments);
        EasyMock.verify(query, parameter);
    }

    @Test
    public void bindNamedParameters() throws NoSuchMethodException {
        final Method method = String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class });
        final Object[] arguments = new Object[] { "tony", 0 };
        final Query query = EasyMock.createMock(Query.class);
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        final Parameter<?> parameter2 = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new HashSet<Parameter<?>>();
        parameters.add(parameter);
        parameters.add(parameter2);

        query.getParameters();
        EasyMock.expectLastCall().andReturn(parameters);
        parameter.getParameterType();
        EasyMock.expectLastCall().andReturn(String.class);
        parameter.getName();
        EasyMock.expectLastCall().andReturn("name").times(3);
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(null).times(2);
        query.setParameter("name", "tony");
        EasyMock.expectLastCall().andReturn(query);
        parameter2.getParameterType();
        EasyMock.expectLastCall().andReturn(Integer.class);
        parameter2.getName();
        EasyMock.expectLastCall().andReturn("start").times(3);
        parameter2.getPosition();
        EasyMock.expectLastCall().andReturn(null).times(2);
        query.setParameter("start", 0);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(query, parameter, parameter2);

        queryParameterBinder.bind(query, method, arguments);
        EasyMock.verify(query, parameter, parameter2);
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
