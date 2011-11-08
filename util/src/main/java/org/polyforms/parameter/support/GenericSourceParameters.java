package org.polyforms.parameter.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.Parameters;
import org.polyforms.parameter.SourceParameters;

public class GenericSourceParameters implements SourceParameters {
    private final Parameters<?> parameters;
    private Map<String, Parameter> namedParameters;
    private Map<Class<?>, List<Parameter>> typedParameters;
    private Map<Integer, Parameter> indexedParameters;

    public GenericSourceParameters(final Parameters<?> parameters) {
        this.parameters = parameters;
    }

    public Parameter match(final Parameter parameter) {
        if (namedParameters == null) {
            namedParameters = new HashMap<String, Parameter>();
            for (final Parameter sourceParameter : parameters.getParameters()) {
                namedParameters.put(sourceParameter.getName(), sourceParameter);
            }
        }
        Parameter matchedParameter = namedParameters.get(parameter.getName());

        if (typedParameters == null) {
            typedParameters = new HashMap<Class<?>, List<Parameter>>();
            for (final Parameter sourceParameter : parameters.getParameters()) {
                final Class<?> type = sourceParameter.getType();
                if (!typedParameters.containsKey(type)) {
                    typedParameters.put(type, new ArrayList<Parameter>());
                }
                typedParameters.get(type).add(sourceParameter);
            }
        }
        if (matchedParameter == null) {
            final List<Parameter> matchedParameters = typedParameters.get(parameter.getType());
            if (matchedParameters != null && matchedParameters.size() == 1) {
                matchedParameter = matchedParameters.get(0);
            }
        }

        if (indexedParameters == null) {
            indexedParameters = new HashMap<Integer, Parameter>();
            for (final Parameter sourceParameter : parameters.getParameters()) {
                indexedParameters.put(sourceParameter.getIndex(), sourceParameter);
            }
        }
        if (matchedParameter == null) {
            matchedParameter = indexedParameters.get(parameter.getIndex());
        }

        if (matchedParameter == null) {
            throw new IllegalArgumentException("Cannot find matched parameter for " + parameter);
        }

        return matchedParameter;
    }
}
