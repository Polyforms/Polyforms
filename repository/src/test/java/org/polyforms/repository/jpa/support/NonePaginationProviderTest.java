package org.polyforms.repository.jpa.support;

import junit.framework.Assert;

import org.junit.Test;
import org.polyforms.repository.jpa.PaginationProvider;

public class NonePaginationProviderTest {
    @Test
    public void noPagination() {
        final PaginationProvider paginationProvider = new NonePaginationProvider();
        Assert.assertEquals(0, paginationProvider.getFirstResult());
        Assert.assertEquals(Integer.MAX_VALUE, paginationProvider.getMaxResults());
    }
}
