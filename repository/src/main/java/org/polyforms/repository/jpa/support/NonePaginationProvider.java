package org.polyforms.repository.jpa.support;

import javax.inject.Named;

import org.polyforms.repository.jpa.PaginationProvider;

/**
 * Implementation of {@link PaginationProvider} which gets all query results without pagination.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class NonePaginationProvider implements PaginationProvider {
    /**
     * {@inheritDoc}
     */
    public int getFirstResult() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxResults() {
        return Integer.MAX_VALUE;
    }
}
