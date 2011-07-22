package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;

/**
 * Implementation of findAll method which finds all entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class FindAll implements Executor {
    private static final String SQL_TEMPLATE = "select e from %s e";
    @PersistenceContext
    private EntityManager entityManager;
    private final EntityClassResolver entityClassResolver;

    /**
     * Create an instance with {@link EntityClassResolver}.
     */
    @Inject
    public FindAll(final EntityClassResolver entityClassResolver) {
        this.entityClassResolver = entityClassResolver;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Object target, final Method method, final Object... arguments) {
        final Class<?> entityClass = entityClassResolver.resolve(target.getClass());
        final String sql = String.format(SQL_TEMPLATE, entityClass.getSimpleName());
        return entityManager.createQuery(sql).getResultList();
    }
}
