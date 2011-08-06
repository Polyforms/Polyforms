package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.QueryNameResolver;
import org.polyforms.repository.spi.EntityClassResolver;

public class EntityClassPrefixingQueryNameResolverTest {
    private EntityClassResolver entityClassResolver;
    private QueryNameResolver queryNameResolver;

    @Before
    public void setUp() {
        entityClassResolver = EasyMock.createMock(EntityClassResolver.class);
        queryNameResolver = new EntityClassPrefixingQueryNameResolver(entityClassResolver);
    }

    @Test
    public void getQueryName() throws NoSuchMethodException {
        entityClassResolver.resolve(Object.class);
        EasyMock.expectLastCall().andReturn(Object.class);
        EasyMock.replay(entityClassResolver);

        final Method method = Object.class.getMethod("toString", new Class<?>[0]);
        Assert.assertEquals("Object.toString", queryNameResolver.getQueryName(method));
        // Just for testing cache.
        Assert.assertEquals("Object.toString", queryNameResolver.getQueryName(method));
        EasyMock.verify(entityClassResolver);
    }
}
