package org.polyforms.parameter.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.Parameters;

class SourceParameters {
    private final Parameters<?> parameters;
    private Map<String, Parameter> namedParameters;
    private Map<Class<?>, Set<Parameter>> typedParameters;
    private Map<Integer, Parameter> indexedParameters;

    protected SourceParameters(final Parameters<?> parameters) {
        this.parameters = parameters;
    }

    protected Parameter match(final Parameter parameter) {
        Parameter matchedParameter = matchByName(parameter);

        if (matchedParameter == null) {
            matchedParameter = matchByType(parameter);
        }

        if (matchedParameter == null) {
            matchedParameter = matchByIndex(parameter);
        }

        if (matchedParameter == null) {
            throw new IllegalArgumentException("Cannot find matched parameter for " + parameter);
        }

        return matchedParameter;
    }

    private Parameter matchByName(final Parameter parameter) {
        if (namedParameters == null) {
            prepareNamedParameters();
        }
        return namedParameters.get(parameter.getName());
    }

    private void prepareNamedParameters() {
        namedParameters = new HashMap<String, Parameter>();
        for (final Parameter sourceParameter : parameters.getParameters()) {
            namedParameters.put(sourceParameter.getName(), sourceParameter);
        }
    }

    private Parameter matchByType(final Parameter parameter) {
        if (typedParameters == null) {
            prepareTypedParameters();
        }
        final Set<Parameter> matchedParameters = typedParameters.get(parameter.getType());
        if (matchedParameters == null || matchedParameters.size() > 1) {
            return null;
        }

        return matchedParameters.iterator().next();
    }

    private void prepareTypedParameters() {
        typedParameters = new HashMap<Class<?>, Set<Parameter>>();
        for (final Parameter sourceParameter : parameters.getParameters()) {
            final Class<?> type = sourceParameter.getType();
            if (!typedParameters.containsKey(type)) {
                typedParameters.put(type, new HashSet<Parameter>());
            }
            typedParameters.get(type).add(sourceParameter);
        }
    }

    private Parameter matchByIndex(final Parameter parameter) {
        if (indexedParameters == null) {
            prepareIndexedParameters();
        }
        return indexedParameters.get(parameter.getIndex());
    }

    private void prepareIndexedParameters() {
        indexedParameters = new HashMap<Integer, Parameter>();
        for (final Parameter sourceParameter : parameters.getParameters()) {
            indexedParameters.put(sourceParameter.getIndex(), sourceParameter);
        }
    }
}
