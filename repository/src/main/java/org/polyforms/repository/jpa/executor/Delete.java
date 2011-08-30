package org.polyforms.repository.jpa.executor;

import javax.inject.Named;

/**
 * Implementation of remove method which remove entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class Delete extends VarArgsExecutor {
    @Override
    protected void doExecute(final Object entity) {
        getEntityManager().remove(entity);
    }
}
