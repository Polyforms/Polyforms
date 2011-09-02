package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

/**
 * Parameter matcher matching parameters by type.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
abstract class TypedParameterMatcher<T> extends ParameterMatcher<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypedParameterMatcher.class);

    @Override
    protected Map<T, Integer> match(final Method method, final Set<Parameter<?>> parameters) {
        final Map<Class<?>, Integer> parameterTypeMap = createArgumentMap(method.getParameterTypes());
        if (parameterTypeMap == null) {
            return null;
        }

        final Map<T, Integer> parameterMap = new HashMap<T, Integer>();
        for (final Parameter<?> parameter : parameters) {
            final Class<?> parameterType = ClassUtils.resolvePrimitiveIfNecessary(parameter.getParameterType());
            if (!parameterTypeMap.containsKey(parameterType)) {
                LOGGER.debug("The JPA parameters of {} is more than 1.", parameterType);
                return null;
            }
            final Integer argument = parameterTypeMap.remove(parameterType);
            parameterMap.put(getKey(parameter), argument);
        }
        return parameterMap;
    }

    private Map<Class<?>, Integer> createArgumentMap(final Class<?>[] parameterTypes) {
        final Map<Class<?>, Integer> parameterTypeMap = new HashMap<Class<?>, Integer>();
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = ClassUtils.resolvePrimitiveIfNecessary(parameterTypes[i]);
            if (parameterTypeMap.containsKey(parameterType)) {
                LOGGER.debug("The parameters of {} is more than 1.", parameterType);
                return null;
            }
            parameterTypeMap.put(parameterType, i);
        }
        return parameterTypeMap;
    }

    protected abstract T getKey(Parameter<?> parameter);
}
