package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

class NamedParameterMatcher implements ParameterMatcher<String> {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public Map<String, Integer> match(final Method method, final Set<Parameter<?>> parameters) {
        final Map<String, Integer> parameterMap = new HashMap<String, Integer>();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        if (parameterNames == null) {
            parameterNames = new String[parameters.size()];
            int i = 0;
            for (final Parameter<?> parameter : parameters) {
                parameterNames[i++] = parameter.getName();
            }
            Arrays.sort(parameterNames);
        }
        for (int i = 0; i < parameterNames.length; i++) {
            parameterMap.put(parameterNames[i], i);
        }
        return parameterMap;
    }
}
