package org.polyforms.repository.jpa.support;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.util.ReflectionUtils;

/**
 * Implementation of {@link EntityHelper} for JPA 2.0.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class Jpa2EntityHelper implements EntityHelper {
    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends Member>, ValueGetter> valueGetters = new HashMap<Class<? extends Member>, ValueGetter>();
    private final Map<Class<?>, SingularAttribute<?, ?>> idAttibuteCache = new HashMap<Class<?>, SingularAttribute<?, ?>>();
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an instance with {@link EntityManager}.
     */
    public Jpa2EntityHelper() {
        valueGetters.put(Field.class, new FieldValueGetter());
        valueGetters.put(Method.class, new PropertyValueGetter());
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEntity(final Class<?> candidate) {
        try {
            return getIdentifierAttribute(candidate) != null;
        } catch (final IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getIdentifierName(final Class<?> entityClass) {
        return getIdentifierAttribute(entityClass).getName();
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getIdentifierClass(final Class<?> entityClass) {
        return getIdentifierAttribute(entityClass).getJavaType();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object getIdentifierValue(final Object entity) {
        final Member member = getIdentifierAttribute(entity.getClass()).getJavaMember();
        return valueGetters.get(member.getClass()).getValue(member, entity);
    }

    private SingularAttribute<?, ?> getIdentifierAttribute(final Class<?> entityClass) {
        if (!idAttibuteCache.containsKey(entityClass)) {
            final EntityType<?> type = getEntityType(entityClass);
            // patch for openjpa because it returns null instead of IllegalArgumentException
            // when class is not an entity.
            if (type == null) {
                throw new IllegalArgumentException("Not an entity: " + entityClass);
            }
            idAttibuteCache.put(entityClass, type.getId(type.getIdType().getJavaType()));
        }
        return idAttibuteCache.get(entityClass);
    }

    private <T> EntityType<T> getEntityType(final Class<T> entityClass) {
        return entityManager.getMetamodel().entity(entityClass);
    }

    private interface ValueGetter<T extends Member> {
        /**
         * Get value from specific object using member like {@link Field} or {@link Method}.
         */
        Object getValue(T member, Object target);
    }

    private static final class PropertyValueGetter implements ValueGetter<Method> {
        /**
         * {@inheritDoc}
         */
        public Object getValue(final Method method, final Object target) {
            ReflectionUtils.makeAccessible(method);
            return ReflectionUtils.invokeMethod(method, target);
        }
    }

    private static final class FieldValueGetter implements ValueGetter<Field> {
        /**
         * {@inheritDoc}
         */
        public Object getValue(final Field field, final Object target) {
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, target);
        }
    }
}
