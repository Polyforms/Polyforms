package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.polyforms.repository.jpa.EntityHelper;
import org.polyforms.repository.spi.EntityClassResolver;
import org.polyforms.repository.spi.Executor;

@Named
public class Find implements Executor {
    private static final String PARAMETER_NAME = "identifiers";
    private static final String SQL_TEMPLATE = "select e from %s e where e.%s in :%s";
    @PersistenceContext
    private EntityManager entityManager;
    private final EntityHelper entityHelper;
    private final EntityClassResolver entityClassResolver;

    @Inject
    public Find(final EntityHelper entityHelper, final EntityClassResolver entityClassResolver) {
        this.entityHelper = entityHelper;
        this.entityClassResolver = entityClassResolver;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Object target, final Method method, final Object... arguments) {
        if (arguments.length == 0) {
            return Collections.EMPTY_LIST;
        }

        final Class<?> entityClass = entityClassResolver.resolve(target.getClass());
        final String queryString = String.format(SQL_TEMPLATE, entityClass.getSimpleName(),
                entityHelper.getIdentifierName(entityClass), PARAMETER_NAME);
        final Query query = entityManager.createQuery(queryString);
        query.setParameter(PARAMETER_NAME, Arrays.asList((Object[]) arguments[0]));
        return query.getResultList();
    }
}
