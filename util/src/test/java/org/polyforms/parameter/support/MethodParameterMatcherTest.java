package org.polyforms.parameter.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;
import org.polyforms.parameter.ArgumentProvider;
import org.polyforms.parameter.annotation.At;

public class MethodParameterMatcherTest {
    private final MethodParameterMatcher methodParameterMatcher = new MethodParameterMatcher();

    @Test
    public void match() throws NoSuchMethodException {
        final Method sourceMethod = this.getClass().getMethod("sourceMethod",
                new Class<?>[] { String.class, int.class });
        final Method targetMethod = this.getClass().getMethod("targetMethod",
                new Class<?>[] { long.class, String.class });

        final ArgumentProvider[] argumentProviders = methodParameterMatcher.match(new MethodParameters(this.getClass(),
                sourceMethod, true), new MethodParameters(this.getClass(), targetMethod, false));
        Assert.assertEquals(2, argumentProviders.length);

        final Object[] arguments = new Object[] { "test", 1 };
        Assert.assertEquals(1, argumentProviders[0].get(arguments));
        Assert.assertEquals("test", argumentProviders[1].get(arguments));
    }

    public void sourceMethod(final String name, final int index) {
    }

    public void targetMethod(@At(1) final long start, final String string) {
    }
}
