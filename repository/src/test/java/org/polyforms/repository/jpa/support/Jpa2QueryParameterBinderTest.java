package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Parameter;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.test.util.ReflectionTestUtils;

public class Jpa2QueryParameterBinderTest {
    private final Object entity = new Object();
    private Method method;
    private Query query;

    private ParameterNameDiscoverer parameterNameDiscoverer;
    private QueryParameterBinder queryParameterBinder;

    @Before
    public void setUp() throws NoSuchMethodException {
        method = MockRepository.class.getMethod("getByName", new Class<?>[] { String.class });
        query = EasyMock.createMock(Query.class);

        parameterNameDiscoverer = EasyMock.createMock(ParameterNameDiscoverer.class);
        queryParameterBinder = new Jpa2QueryParameterBinder();
        ReflectionTestUtils.setField(queryParameterBinder, "parameterNameDiscoverer", parameterNameDiscoverer);
    }

    @Test
    public void bindPositionalParametersByType() {
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        query.getParameters();
        EasyMock.expectLastCall().andReturn(Collections.singleton(parameter));
        parameter.getParameterType();
        EasyMock.expectLastCall().andReturn(Object.class);
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(1).times(2);
        query.setParameter(1, entity);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(query, parameter);

        queryParameterBinder.bind(query, method, new Object[] { entity });
        EasyMock.verify(query, parameter);
    }

    @Test
    public void bindNamedParametersByType() {
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        query.getParameters();
        EasyMock.expectLastCall().andReturn(Collections.singleton(parameter));
        parameter.getParameterType();
        EasyMock.expectLastCall().andReturn(Object.class);
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(null);
        parameter.getName();
        EasyMock.expectLastCall().andReturn("name");
        query.setParameter("name", entity);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(query, parameter);

        queryParameterBinder.bind(query, method, new Object[] { entity });
        EasyMock.verify(query, parameter);
    }

    @Test
    public void bindPositionalParameters() {
        ReflectionTestUtils.setField(queryParameterBinder, "parameterNameDiscoverer", parameterNameDiscoverer);
        final Parameter<?> parameter1 = EasyMock.createMock(Parameter.class);
        final Parameter<?> parameter2 = EasyMock.createMock(Parameter.class);
        unmatchedParameters(parameter1, parameter2);
        parameter1.getPosition();
        EasyMock.expectLastCall().andReturn(1).times(2);
        parameter2.getPosition();
        EasyMock.expectLastCall().andReturn(2);
        query.setParameter(1, entity);
        EasyMock.expectLastCall().andReturn(query);
        query.setParameter(2, entity);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(query, parameter1, parameter2);

        queryParameterBinder.bind(query, method, new Object[] { entity, entity });
        EasyMock.verify(query, parameter1, parameter2);
    }

    private void unmatchedParameters(final Parameter<?> parameter1, final Parameter<?> parameter2) {
        final Set<Parameter<?>> parameters = new TreeSet<Parameter<?>>(new Comparator<Parameter<?>>() {
            public int compare(final Parameter<?> o1, final Parameter<?> o2) {
                return o1 == parameter1 ? -1 : 1;
            }
        });
        parameters.add(parameter1);
        parameters.add(parameter2);
        query.getParameters();
        EasyMock.expectLastCall().andReturn(parameters);
        parameter1.getParameterType();
        EasyMock.expectLastCall().andReturn(Object.class);
        parameter2.getParameterType();
        EasyMock.expectLastCall().andReturn(Object.class);
    }

    @Test
    public void bindNamedParamentersWhichNameIsFromMethod() {
        final Parameter<?> parameter1 = EasyMock.createMock(Parameter.class);
        final Parameter<?> parameter2 = EasyMock.createMock(Parameter.class);
        unmatchedParameters(parameter1, parameter2);
        parameter1.getPosition();
        EasyMock.expectLastCall().andReturn(null);
        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(new String[] { "code", "name" });
        query.setParameter("name", entity);
        EasyMock.expectLastCall().andReturn(query);
        query.setParameter("code", entity);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(parameterNameDiscoverer, query, parameter1, parameter2);

        queryParameterBinder.bind(query, method, new Object[] { entity, entity });
        EasyMock.verify(parameterNameDiscoverer, query, parameter1, parameter2);
    }

    @Test
    public void bindNamedParamentersWhichNameIsFromQuery() {
        final Parameter<?> parameter1 = EasyMock.createMock(Parameter.class);
        final Parameter<?> parameter2 = EasyMock.createMock(Parameter.class);
        unmatchedParameters(parameter1, parameter2);
        parameter1.getPosition();
        EasyMock.expectLastCall().andReturn(null);
        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(null);
        parameter1.getName();
        EasyMock.expectLastCall().andReturn("name");
        query.setParameter("name", entity);
        EasyMock.expectLastCall().andReturn(query);
        parameter2.getName();
        EasyMock.expectLastCall().andReturn("code");
        query.setParameter("code", entity);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(parameterNameDiscoverer, query, parameter1, parameter2);

        queryParameterBinder.bind(query, method, new Object[] { entity, entity });
        EasyMock.verify(parameterNameDiscoverer, query, parameter1, parameter2);
    }

    @Test
    public void noArgumentsToBind() {
        query.getParameters();
        EasyMock.expectLastCall().andReturn(Collections.EMPTY_SET);
        EasyMock.replay(query);

        queryParameterBinder.bind(query, method, new Object[0]);
        EasyMock.verify(query);
    }

    private static interface MockRepository {
        Object getByName(String name);
    }
}
