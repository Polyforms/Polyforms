package org.polyforms.repository.jpa.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import javax.inject.Named;
import javax.persistence.Parameter;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryParameterBinder;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

@Named
public class Jpa2QueryParameterBinder implements QueryParameterBinder {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public void bind(final Query query, final Method method, final Object[] arguments) {
        final Set<Parameter<?>> jpaParameters = query.getParameters();
        if (jpaParameters.isEmpty()) {
            return;
        }

        if (isPositionalParameters(jpaParameters)) {
            setPositionalParameters(query, jpaParameters, arguments);
        } else {
            final String[] parameterNames = getParameterNames(method, jpaParameters);
            setNamedParameters(query, parameterNames, arguments);
        }
    }

    private boolean isPositionalParameters(final Set<Parameter<?>> jpaParameters) {
        return jpaParameters.iterator().next().getPosition() != null;
    }

    private void setPositionalParameters(final Query query, final Set<Parameter<?>> jpaParameters,
            final Object[] arguments) {
        for (final Parameter<?> parameter : jpaParameters) {
            final int position = parameter.getPosition();
            query.setParameter(position, arguments[position - 1]);
        }
    }

    private void setNamedParameters(final Query query, final String[] parameterNames, final Object[] arguments) {
        for (int i = 0; i < parameterNames.length; i++) {
            query.setParameter(parameterNames[i], arguments[i]);
        }
    }

    private String[] getParameterNames(final Method method, final Set<Parameter<?>> jpaParameters) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        if (parameterNames == null) {
            parameterNames = new String[jpaParameters.size()];
            int i = 0;
            for (final Parameter<?> jpaParameter : jpaParameters) {
                parameterNames[i++] = jpaParameter.getName();
            }
            Arrays.sort(parameterNames);
        }
        return parameterNames;
    }
}
