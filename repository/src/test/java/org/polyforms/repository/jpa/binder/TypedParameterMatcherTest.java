package org.polyforms.repository.jpa.binder;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Parameter;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class TypedParameterMatcherTest implements ParameterKey<String> {
    private ParameterMatcher<String> parameterMatcher;

    @Before
    public void setUp() {
        parameterMatcher = new TypedParameterMatcher<String>(this);
    }

    @Test
    public void match() throws NoSuchMethodException {
        final Parameter<?> parameter1 = EasyMock.createMock(Parameter.class);
        final Parameter<?> parameter2 = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new TreeSet<Parameter<?>>(new Comparator<Parameter<?>>() {
            public int compare(final Parameter<?> o1, final Parameter<?> o2) {
                return o1 == parameter1 ? -1 : 1;
            }
        });
        parameters.add(parameter1);
        parameters.add(parameter2);

        parameter1.getParameterType();
        EasyMock.expectLastCall().andReturn(String.class);
        parameter1.getName();
        EasyMock.expectLastCall().andReturn("code");
        parameter2.getParameterType();
        EasyMock.expectLastCall().andReturn(Integer.class);
        parameter2.getName();
        EasyMock.expectLastCall().andReturn("name");
        EasyMock.replay(parameter1, parameter2);

        final Map<String, Integer> parameterMap = parameterMatcher.match(
                String.class.getMethod("startsWith", new Class<?>[] { String.class, int.class }), parameters);
        Assert.assertEquals(2, parameterMap.size());
        Assert.assertEquals(0, parameterMap.get("code").intValue());
        Assert.assertEquals(1, parameterMap.get("name").intValue());
        EasyMock.verify(parameter1, parameter2);
    }

    @Test
    public void duplicateParameterTypes() throws NoSuchMethodException {
        final Parameter<?> parameter1 = EasyMock.createMock(Parameter.class);
        final Parameter<?> parameter2 = EasyMock.createMock(Parameter.class);
        final Set<Parameter<?>> parameters = new TreeSet<Parameter<?>>(new Comparator<Parameter<?>>() {
            public int compare(final Parameter<?> o1, final Parameter<?> o2) {
                return o1 == parameter1 ? -1 : 1;
            }
        });
        parameters.add(parameter1);
        parameters.add(parameter2);

        parameter1.getParameterType();
        EasyMock.expectLastCall().andReturn(String.class);
        parameter1.getName();
        EasyMock.expectLastCall().andReturn("code");
        parameter2.getParameterType();
        EasyMock.expectLastCall().andReturn(String.class);
        EasyMock.replay(parameter1, parameter2);

        Assert.assertNull(parameterMatcher.match(
                String.class.getMethod("startsWith", new Class<?>[] { String.class, int.class }), parameters));
        EasyMock.verify(parameter1, parameter2);
    }

    @Test
    public void duplicateParameterTypesInMethod() throws NoSuchMethodException {
        Assert.assertNull(parameterMatcher.match(
                String.class.getMethod("substring", new Class<?>[] { int.class, int.class }), null));
    }

    public String getKey(final Parameter<?> parameter) {
        return parameter.getName();
    }
}
