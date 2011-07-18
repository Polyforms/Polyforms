package org.polyforms.repository;

import java.util.List;

/**
 * A read only {@link Repository} which has some convenient methods to retrieve instance(s) of entity class specified in
 * generic type parameter.
 * 
 * @param <T> type of entity
 * @param <PK> type of identifier in entity
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface ReadOnlyRepository<T, PK> extends Repository<T> {
    /**
     * Get an entity by identifier.
     * 
     * Search for an entity instance of the specified identifier. If the entity instance is contained in repository, it
     * is returned from there.
     * 
     * @param identifier of entity instance
     * 
     * @return the found entity instance or <b>null</b> if the entity does not exist
     */
    T get(PK identifier);

    /**
     * Load all instances of specified entity class.
     * 
     * @return a list of all instances or empty list if no instance for specified entity class
     */
    List<T> findAll();

    /**
     * Load entities by specified identifiers.
     * 
     * @param identifiers a list of identifier of entity instances
     * 
     * @return a list of found entity instances or empty list if no instance matching
     */
    List<T> find(PK... identifiers);
}
