package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.Executor;

/**
 * Implementation of save method which persist entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class Save implements Executor {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    public Object execute(final Object target, final Method method, final Object... arguments) {
        if (arguments.length > 0) {
            final Object argument = arguments[0];
            if (method.isVarArgs()) {
                for (final Object entity : (Object[]) argument) {
                    save(entity);
                }
            } else {
                save(argument);
            }
        }

        return Void.TYPE;
    }

    private void save(final Object entity) {
        entityManager.persist(entity);
    }
}
