package org.polyforms.repository.jpa.querydsl;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.EntityClassResolver;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;

/**
 * Implementation of method which deletes matching entities by QueryDSL.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named("deleteByDSL")
public class Delete extends QueryDslExecutor {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an instance with {@link EntityClassResolver}.
     */
    @Inject
    public Delete(final EntityClassResolver entityClassResolver) {
        super(entityClassResolver);
    }

    @Override
    protected Object getResult(final EntityPath<?> entityPath, final Method method, final Object... arguments) {
        final JPADeleteClause deleteClause = new JPADeleteClause(entityManager, entityPath)
                .where((Predicate) arguments[0]);
        return deleteClause.execute();
    }
}
