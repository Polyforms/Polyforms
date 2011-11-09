package org.polyforms.parameter.provider;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.test.util.ReflectionTestUtils;

public class ArgumentNamedTest {
    @Test
    public void get() throws NoSuchMethodException {
        final Method method = String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class });
        final ParameterNameDiscoverer parameterNameDiscoverer = EasyMock.createMock(ParameterNameDiscoverer.class);
        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(new String[] { "name", "start" });
        EasyMock.replay(parameterNameDiscoverer);

        final ArgumentProvider provider = new ArgumentNamed("name");
        ReflectionTestUtils.setField(provider, "parameterNameDiscoverer", parameterNameDiscoverer);
        provider.validate(method);
        Assert.assertEquals("test", provider.get(new Object[] { "test", 0 }));
        EasyMock.verify(parameterNameDiscoverer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBeforeValidation() throws NoSuchMethodException {
        final ArgumentProvider provider = new ArgumentNamed("name");
        provider.get(new Object[] { "test", 0 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullName() {
        new ArgumentNamed(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() {
        new ArgumentNamed("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noParameterNames() throws NoSuchMethodException {
        final Method method = String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class });
        final ParameterNameDiscoverer parameterNameDiscoverer = EasyMock.createMock(ParameterNameDiscoverer.class);
        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(null);
        EasyMock.replay(parameterNameDiscoverer);

        final ArgumentProvider provider = new ArgumentNamed("name");
        ReflectionTestUtils.setField(provider, "parameterNameDiscoverer", parameterNameDiscoverer);
        provider.validate(method);
        EasyMock.verify(parameterNameDiscoverer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noMatchedParameterName() throws NoSuchMethodException {
        final Method method = String.class.getMethod("indexOf", new Class<?>[] { String.class, int.class });
        final ParameterNameDiscoverer parameterNameDiscoverer = EasyMock.createMock(ParameterNameDiscoverer.class);
        parameterNameDiscoverer.getParameterNames(method);
        EasyMock.expectLastCall().andReturn(new String[] { "string", "start" });
        EasyMock.replay(parameterNameDiscoverer);

        final ArgumentProvider provider = new ArgumentNamed("name");
        ReflectionTestUtils.setField(provider, "parameterNameDiscoverer", parameterNameDiscoverer);
        provider.validate(method);
        EasyMock.verify(parameterNameDiscoverer);
    }
}
