package org.polyforms.repository.jpa;

/**
 * Interface to provide pagination information for query.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface PaginationProvider {
    /**
     * The position of the first result the query object to retrieve.
     * 
     * @return position of the first result
     */
    int getFirstResult();

    /**
     * The maximum number of results the query object to retrieve.
     * 
     * @return maximum number of results
     */
    int getMaxResults();
}
