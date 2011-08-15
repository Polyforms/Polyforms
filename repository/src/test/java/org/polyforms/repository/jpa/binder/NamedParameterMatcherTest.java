package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.test.util.ReflectionTestUtils;

public class NamedParameterMatcherTest {
    private ParameterNameDiscoverer parameterNameDiscoverer;
    private ParameterMatcher<String> parameterMatcher;
    private Method method;

    @Before
    public void setUp() throws NoSuchMethodException {
        parameterNameDiscoverer = EasyMock.createMock(ParameterNameDiscoverer.class);
        parameterMatcher = new NamedParameterMatcher();
        ReflectionTestUtils.setField(parameterMatcher, "parameterNameDiscoverer", parameterNameDiscoverer);
        method = String.class.getMethod("length", new Class<?>[0]);
    }

    @Test
    public void matchMethodParameters() {
        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(new String[] { "name", "code" });
        EasyMock.replay(parameterNameDiscoverer);

        final Map<String, Integer> parameterMap = parameterMatcher.match(method, null);
        Assert.assertEquals(2, parameterMap.size());
        Assert.assertEquals(0, parameterMap.get("name").intValue());
        Assert.assertEquals(1, parameterMap.get("code").intValue());
        EasyMock.verify(parameterNameDiscoverer);
    }

    @Test
    public void matchQueryParameters() {
        final Parameter<?> parameter1 = EasyMock.createMock(Parameter.class);
        final Parameter<?> parameter2 = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new HashSet<Parameter<?>>();
        parameters.add(parameter1);
        parameters.add(parameter2);

        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(null);
        parameter1.getName();
        EasyMock.expectLastCall().andReturn("name");
        parameter2.getName();
        EasyMock.expectLastCall().andReturn("code");
        EasyMock.replay(parameterNameDiscoverer, parameter1, parameter2);

        final Map<String, Integer> parameterMap = parameterMatcher.match(method, parameters);
        Assert.assertEquals(2, parameterMap.size());
        Assert.assertEquals(0, parameterMap.get("code").intValue());
        Assert.assertEquals(1, parameterMap.get("name").intValue());
        EasyMock.verify(parameterNameDiscoverer, parameter1, parameter2);
    }
}
