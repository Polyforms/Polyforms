package org.polyforms.repository;

/**
 * A updatable {@link Repository} which has some convenient methods to update entity instance of entity class specified
 * in generic type parameter.
 * 
 * @param <T> type of entity
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface UpdatableRepository<T> extends Repository<T> {
    /**
     * Add entity instances into repository.
     * 
     * @param entities which are to be added into repository
     */
    void save(T... entities);

    /**
     * Remove specified entity instances from repository.
     * 
     * @param entities which are to be removed from repository
     */
    void remove(T... entities);
}
