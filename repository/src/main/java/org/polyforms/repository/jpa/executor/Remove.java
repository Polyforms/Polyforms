package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.Executor;

@Named
public final class Remove implements Executor {
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
                    remove(entity);
                }
            } else {
                remove(argument);
            }
        }

        return Void.TYPE;
    }

    private void remove(final Object entity) {
        entityManager.remove(entity);
    }
}
