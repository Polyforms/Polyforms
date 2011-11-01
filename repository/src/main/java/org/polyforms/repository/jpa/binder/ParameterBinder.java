package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Parameter;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of {@link ParameterBinder} for JPA 2.0 which binds parameters by pairs matched by matchers.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
abstract class ParameterBinder<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterBinder.class);
    private final List<ParameterMatcher<T>> parameterMatchers = new ArrayList<ParameterMatcher<T>>();
    private final Map<Method, Map<T, Integer>> parameterMatchingCache = new HashMap<Method, Map<T, Integer>>();

    protected ParameterBinder() {
        addParameterMatcher(new TypedParameterMatcher<T>() {
            @Override
            protected T getKey(final Parameter<?> parameter) {
                return ParameterBinder.this.getKey(parameter);
            }
        });
    }

    protected final void addParameterMatcher(final ParameterMatcher<T> parameterMatcher) {
        this.parameterMatchers.add(parameterMatcher);
    }

    protected void bind(final Query query, final Method method, final Object[] arguments) {
        final Map<T, Integer> argumentMap = matchParameters(method, query.getParameters());
        LOGGER.debug("Binding parameters for {} by matching {}.", method, argumentMap);
        for (final Entry<T, Integer> entry : argumentMap.entrySet()) {
            setParameter(query, entry.getKey(), arguments[entry.getValue()]);
        }
    }

    private Map<T, Integer> matchParameters(final Method method, final Set<Parameter<?>> parameters) {
        if (!parameterMatchingCache.containsKey(method)) {
            LOGGER.trace("Cache miss for matching parameters of {}.", method);
            LOGGER.trace("Matching parameters of {}.", method);
            for (final ParameterMatcher<T> parameterMatcher : parameterMatchers) {
                final Map<T, Integer> parameterMap = parameterMatcher.match(method, parameters);
                if (parameterMap != null) {
                    LOGGER.debug("Parameter matched by {} for {}.", parameterMatcher.getClass().getSimpleName(), method);
                    parameterMatchingCache.put(method, parameterMap);
                    break;
                }
            }
        }
        return parameterMatchingCache.get(method);
    }

    protected abstract void setParameter(Query query, T key, Object value);

    protected abstract T getKey(Parameter<?> parameter);
}
