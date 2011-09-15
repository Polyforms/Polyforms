package org.polyforms.repository.jpa.querydsl;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.polyforms.repository.spi.EntityClassResolver;

import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * Implementation of method which updates matching entities by QueryDSL.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named("updateByDSL")
public class Update extends QueryDslExecutor {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an instance with {@link EntityClassResolver}.
     */
    @Inject
    public Update(final EntityClassResolver entityClassResolver) {
        super(entityClassResolver);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object getResult(final EntityPath<?> entityPath, final Method method, final Object... arguments) {
        final JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, entityPath).where(
                (Predicate) arguments[0]).set((List<? extends Path<?>>) arguments[1], (List<?>) arguments[2]);
        return updateClause.execute();
    }
}
