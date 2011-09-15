package org.polyforms.repository.jpa.querydsl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.mysema.query.types.EntityPath;

/**
 * Utilities for working with QueryDSL.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class QueryDslUtils {
    private static final Map<Class<?>, EntityPath<?>> entityPathCache = new HashMap<Class<?>, EntityPath<?>>();

    protected QueryDslUtils() {
        throw new UnsupportedOperationException();
    }

    protected static EntityPath<?> findEntityPath(final Class<?> entityClass) {
        if (!entityPathCache.containsKey(entityClass)) {
            final String queryClassName = getQueryClassName(entityClass);
            try {
                final Class<?> pathClass = ClassUtils.forName(queryClassName, QueryDslUtils.class.getClassLoader());
                final Field field = getStaticFieldOfType(pathClass);

                if (field == null) {
                    throw new IllegalStateException("Cannot find static field of query class " + pathClass);
                } else {
                    entityPathCache.put(entityClass, (EntityPath<?>) ReflectionUtils.getField(field, null));
                }
            } catch (final ClassNotFoundException e) {
                throw new IllegalArgumentException("Cannot find query class of entity " + entityClass.getName());
            }
        }

        return entityPathCache.get(entityClass);
    }

    private static Field getStaticFieldOfType(final Class<?> type) {
        for (final Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && type.equals(field.getType())) {
                return field;
            }
        }

        final Class<?> superclass = type.getSuperclass();
        return Object.class.equals(superclass) ? null : getStaticFieldOfType(superclass);
    }

    private static String getQueryClassName(final Class<?> domainClass) {
        return domainClass.getPackage().getName() + ".Q" + domainClass.getSimpleName().replace(".", "_");
    }
}
