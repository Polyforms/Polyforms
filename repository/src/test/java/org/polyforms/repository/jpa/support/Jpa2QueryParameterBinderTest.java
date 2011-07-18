package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.Collections;

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
    public void bindPositionalParameters() {
        queryParameterBinder = new Jpa2QueryParameterBinder();
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        query.getParameters();
        EasyMock.expectLastCall().andReturn(Collections.singleton(parameter));
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(1).times(2);
        query.setParameter(1, entity);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(query, parameter);

        queryParameterBinder.bind(query, method, new Object[] { entity });
        EasyMock.verify(query, parameter);
    }

    @Test
    public void bindNamedParametersWhichNameIsFromMethod() {
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        query.getParameters();
        EasyMock.expectLastCall().andReturn(Collections.singleton(parameter));
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(null);
        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(new String[] { "name" });
        query.setParameter("name", entity);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(parameterNameDiscoverer, query, parameter);

        queryParameterBinder.bind(query, method, new Object[] { entity });
        EasyMock.verify(parameterNameDiscoverer, query, parameter);
    }

    @Test
    public void bindNamedParamentersWhichNameIsFromQuery() {
        final Parameter<?> parameter = EasyMock.createMock(Parameter.class);
        query.getParameters();
        EasyMock.expectLastCall().andReturn(Collections.singleton(parameter));
        parameter.getPosition();
        EasyMock.expectLastCall().andReturn(null);
        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(null);
        parameter.getName();
        EasyMock.expectLastCall().andReturn("name");
        query.setParameter("name", entity);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(parameterNameDiscoverer, query, parameter);

        queryParameterBinder.bind(query, method, new Object[] { entity });
        EasyMock.verify(parameterNameDiscoverer, query, parameter);
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
