package org.polyforms.repository.jpa.executor;

import javax.inject.Named;

/**
 * Implementation of method which save entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class Save extends VarArgsExecutor {
    @Override
    protected void doExecute(final Object entity) {
        getEntityManager().persist(entity);
    }
}
