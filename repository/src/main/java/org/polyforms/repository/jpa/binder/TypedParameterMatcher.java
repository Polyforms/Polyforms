package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

import org.springframework.util.ClassUtils;

class TypedParameterMatcher<T> implements ParameterMatcher<T> {
    private final ParameterKey<T> parameterKey;

    protected TypedParameterMatcher(final ParameterKey<T> parameterKey) {
        this.parameterKey = parameterKey;
    }

    public Map<T, Integer> match(final Method method, final Set<Parameter<?>> parameters) {
        final Map<Class<?>, Integer> parameterTypeMap = createArgumentMap(method.getParameterTypes());
        if (parameterTypeMap == null) {
            return null;
        }

        final Map<T, Integer> parameterMap = new HashMap<T, Integer>();
        for (final Parameter<?> parameter : parameters) {
            final Class<?> parameterType = ClassUtils.resolvePrimitiveIfNecessary(parameter.getParameterType());
            if (!parameterTypeMap.containsKey(parameterType)) {
                return null;
            }
            final Integer argument = parameterTypeMap.remove(parameterType);
            parameterMap.put(parameterKey.getKey(parameter), argument);
        }
        return parameterMap;
    }

    private Map<Class<?>, Integer> createArgumentMap(final Class<?>[] parameterTypes) {
        final Map<Class<?>, Integer> parameterTypeMap = new HashMap<Class<?>, Integer>();
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = ClassUtils.resolvePrimitiveIfNecessary(parameterTypes[i]);
            if (parameterTypeMap.containsKey(parameterType)) {
                return null;
            }
            parameterTypeMap.put(parameterType, i);
        }
        return parameterTypeMap;
    }
}

interface ParameterKey<T> {
    T getKey(Parameter<?> parameter);
}
