package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;

/**
 * Implementation of {@link QueryBuilder} for NamedQuery.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
class NamedQueryBuilder {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    public Query build(final Class<?> entityClass, final Method method) {
        try {
            return entityManager.createNamedQuery(entityClass.getSimpleName() + "." + method.getName());
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
