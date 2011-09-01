package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * Parameter matcher matching parameters by name.
 * 
 * Firstly, it tries to match by name if parameter name of method can be got (compile with debug information). If not,
 * then it matches JPA parameters (ordered by name) with method's parameter in order.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
class NamedParameterMatcher implements ParameterMatcher<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NamedParameterMatcher.class);
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * {@inheritDoc}
     */
    public Map<String, Integer> match(final Method method, final Set<Parameter<?>> parameters) {
        final Map<String, Integer> parameterMap = new HashMap<String, Integer>();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        if (parameterNames == null) {
            LOGGER.debug("Cannot get parameter names from local variable table. Use name of query parameters instead.");
            parameterNames = getParameterNamesFromQurey(parameters);
        }
        for (int i = 0; i < parameterNames.length; i++) {
            parameterMap.put(parameterNames[i], i);
        }
        return parameterMap;
    }

    private String[] getParameterNamesFromQurey(final Set<Parameter<?>> parameters) {
        final String[] parameterNames = new String[parameters.size()];
        int i = 0;
        for (final Parameter<?> parameter : parameters) {
            parameterNames[i++] = parameter.getName();
        }
        Arrays.sort(parameterNames);
        return parameterNames;
    }
}
