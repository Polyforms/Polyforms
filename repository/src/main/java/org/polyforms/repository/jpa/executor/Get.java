package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;

/**
 * Implementation of get method which finds entity by identifier.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class Get implements Executor {
    @PersistenceContext
    private EntityManager entityManager;
    private final EntityClassResolver entityClassResolver;

    /**
     * Create an instance with {@link EntityClassResolver}.
     */
    @Inject
    public Get(final EntityClassResolver entityClassResolver) {
        this.entityClassResolver = entityClassResolver;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Object target, final Method method, final Object... arguments) {
        if (arguments.length == 0) {
            return null;
        }

        return entityManager.find(entityClassResolver.resolve(target.getClass()), arguments[0]);
    }
}
