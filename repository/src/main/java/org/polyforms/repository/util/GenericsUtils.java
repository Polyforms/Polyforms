package org.polyforms.repository.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

/**
 * Utilities for working with generic by reflection.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class GenericsUtils {
    protected GenericsUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Locates generic declarations on a class.
     * 
     * @param clazz clazz The class to introspect
     * 
     * @return the resolved type of each argument, with the array size matching the number of actual type arguments, or
     *         <code>null</code> if not resolvable
     */
    public static final Class<?>[] resolveTypeArguments(final Class<?> clazz) {
        Assert.notNull(clazz);

        final ParameterizedType parameterizedType = resolveParameterizedType(clazz);
        return parameterizedType == null ? null : GenericTypeResolver.resolveTypeArguments(clazz,
                (Class<?>) parameterizedType.getRawType());
    }

    private static ParameterizedType resolveParameterizedType(final Class<?> clazz) {
        ParameterizedType genericType = resolveFromSuperClass(clazz);

        if (genericType == null) {
            genericType = resolveFromInterfaces(clazz);
        }
        return genericType;
    }

    private static ParameterizedType resolveFromSuperClass(final Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return null;
        }

        final Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            return (ParameterizedType) type;
        }
        return resolveFromSuperClass(clazz.getSuperclass());
    }

    private static ParameterizedType resolveFromInterfaces(final Class<?> clazz) {
        for (final Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                return (ParameterizedType) type;
            }

            final ParameterizedType parameterizedType = resolveParameterizedType((Class<?>) type);
            if (parameterizedType != null) {
                return parameterizedType;
            }
        }
        return null;
    }
}
