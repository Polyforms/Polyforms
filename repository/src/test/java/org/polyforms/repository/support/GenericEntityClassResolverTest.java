package org.polyforms.repository.support;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.Repository;
import org.polyforms.repository.spi.EntityClassResolver;

public class GenericEntityClassResolverTest {
    private EntityClassResolver entityClassResolver;

    @Before
    public void setUp() {
        entityClassResolver = new GenericEntityClassResolver();
    }

    @Test
    public void resolve() {
        Assert.assertSame(Object.class, entityClassResolver.resolve(MockRepository.class));
        // Just for testing cache.
        Assert.assertSame(Object.class, entityClassResolver.resolve(MockRepository.class));
    }

    @Test
    public void resolveFromSpecifiedClass() {
        entityClassResolver = new GenericEntityClassResolver(Repository.class);
        Assert.assertSame(Object.class, entityClassResolver.resolve(MockRepository.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolverWithoutEntityClass() {
        entityClassResolver.resolve(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createInstanceWithNegativePosition() {
        new GenericEntityClassResolver(null, -1);
    }

    public static interface MockRepository extends Repository<Object> {
    }
}
