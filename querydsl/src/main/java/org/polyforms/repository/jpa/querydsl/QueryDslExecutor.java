package org.polyforms.repository.jpa.querydsl;

import java.lang.reflect.Method;

import org.polyforms.repository.spi.ConditionalExecutor;
import org.polyforms.repository.spi.EntityClassResolver;
import org.springframework.util.ClassUtils;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;

/**
 * Abstract implementation of methods which use QueryDSL to do some work in persistence.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public abstract class QueryDslExecutor implements ConditionalExecutor {
    private static final boolean QUERY_DSL_PRESENT = ClassUtils.isPresent("com.mysema.query.types.Predicate",
            QueryDslUtils.class.getClassLoader());
    private final EntityClassResolver entityClassResolver;

    protected QueryDslExecutor(final EntityClassResolver entityClassResolver) {
        this.entityClassResolver = entityClassResolver;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Object target, final Method method, final Object... arguments) {
        final EntityPath<?> entityPath = QueryDslUtils.findEntityPath(entityClassResolver.resolve(target.getClass()));
        return getResult(entityPath, method, arguments);
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length > 0 && QUERY_DSL_PRESENT && parameterTypes[0] == Predicate.class;
    }

    protected abstract Object getResult(EntityPath<?> entityPath, Method method, Object... arguments);
}
