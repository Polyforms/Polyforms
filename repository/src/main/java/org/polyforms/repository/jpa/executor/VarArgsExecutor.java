package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.Executor;

/**
 * Abstract implementation for method which may has variable parameters.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
abstract class VarArgsExecutor implements Executor {
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
                    doExecute(entity);
                }
            } else {
                doExecute(argument);
            }
        }

        return Void.TYPE;
    }

    protected abstract void doExecute(Object entity);

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
