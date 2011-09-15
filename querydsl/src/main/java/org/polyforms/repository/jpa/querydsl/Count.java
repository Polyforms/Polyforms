package org.polyforms.repository.jpa.querydsl;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.EntityClassResolver;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;

/**
 * Implementation of method which returns count of matching entities by QueryDSL.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named("countByDSL")
public class Count extends QueryDslExecutor {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an instance with {@link EntityClassResolver}.
     */
    @Inject
    public Count(final EntityClassResolver entityClassResolver) {
        super(entityClassResolver);
    }

    @Override
    protected Object getResult(final EntityPath<?> entityPath, final Method method, final Object... arguments) {
        final JPAQuery query = new JPAQuery(entityManager).from(entityPath).where((Predicate) arguments[0]);
        return query.count();
    }
}
