package org.polyforms.repository.support;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.Repository;
import org.polyforms.repository.spi.EntityClassResolver;

public class GenericEntityClassResolverTest {
    private EntityClassResolver entityClassResolver;

    @Before
    public void setUp() {
        entityClassResolver = new GenericEntityClassResolver(Repository.class);
    }

    @Test
    public void resolve() {
        Assert.assertSame(Object.class, entityClassResolver.resolve(MockRepository.class));
        // Just for testing cache.
        Assert.assertSame(Object.class, entityClassResolver.resolve(MockRepository.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolverWithoutEntityClass() {
        entityClassResolver.resolve(Object.class);
    }

    public static interface MockRepository extends Repository<Object> {
    }
}
