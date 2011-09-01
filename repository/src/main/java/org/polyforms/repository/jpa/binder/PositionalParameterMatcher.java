package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

/**
 * Parameter matcher matching parameters by position.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class PositionalParameterMatcher implements ParameterMatcher<Integer> {
    /**
     * {@inheritDoc}
     */
    public Map<Integer, Integer> match(final Method method, final Set<Parameter<?>> parameters) {
        final Map<Integer, Integer> parameterMap = new HashMap<Integer, Integer>();
        for (final Parameter<?> parameter : parameters) {
            final Integer position = parameter.getPosition();
            parameterMap.put(position, position - 1);
        }
        return parameterMap;
    }
}
