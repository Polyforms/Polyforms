package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EntityClassPrefixingQueryNameResolverTest {
    private QueryResolver queryNameResolver;

    @Before
    public void setUp() {
        queryNameResolver = new EntityClassPrefixingQueryNameResolver();
    }

    @Test
    public void getQueryName() throws NoSuchMethodException {
        final Method method = Object.class.getMethod("toString", new Class<?>[0]);
        Assert.assertEquals("Object.toString", queryNameResolver.getQuery(Object.class, method));
        // Just for testing cache.
        Assert.assertEquals("Object.toString", queryNameResolver.getQuery(Object.class, method));
    }
}
