package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;

/**
 * Implementation of method which load entity by identifier.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class Load implements Executor {
    @PersistenceContext
    private EntityManager entityManager;
    private final EntityClassResolver entityClassResolver;

    /**
     * Create an instance with {@link EntityClassResolver}.
     */
    @Inject
    public Load(final EntityClassResolver entityClassResolver) {
        this.entityClassResolver = entityClassResolver;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Object target, final Method method, final Object... arguments) {
        if (arguments.length == 0) {
            throw new IllegalArgumentException("The identifier must pass to load entity method.");
        }

        final Object identifier = arguments[0];
        final Class<?> entityClass = entityClassResolver.resolve(target.getClass());
        final Object entity = entityManager.find(entityClass, identifier);
        if (entity == null) {
            throw new EntityNotFoundException("Cannot found entity " + entityClass.getSimpleName()
                    + " with identifier " + identifier + ".");
        }
        return entity;
    }
}
