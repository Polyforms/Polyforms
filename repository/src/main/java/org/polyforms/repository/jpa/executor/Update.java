package org.polyforms.repository.jpa.executor;

import javax.inject.Named;

/**
 * Implementation of method which update entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class Update extends VarArgsExecutor {
    @Override
    protected void doExecute(final Object entity) {
        getEntityManager().merge(entity);
    }
}
