package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.Set;

import javax.inject.Named;
import javax.persistence.Parameter;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryParameterBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The adaptor of {@link ParameterBinder} to {@link QueryParameterBinder}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public class Jpa2QueryParameterBinder implements QueryParameterBinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Jpa2QueryParameterBinder.class);
    private final ParameterBinder<String> namedParameterBinder = new NamedParameterBinder();
    private final ParameterBinder<Integer> positionalParameterBinder = new PositionalParameterBinder();

    /**
     * {@inheritDoc}
     */
    public void bind(final Query query, final Method method, final Object[] arguments) {
        final Set<Parameter<?>> parameters = query.getParameters();
        if (parameters.isEmpty()) {
            LOGGER.debug("No parameters need bind for {}.", method);
            return;
        }

        if (isPositionalParameters(parameters)) {
            LOGGER.debug("Binding positional parameters for {}.", method);
            positionalParameterBinder.bind(query, method, arguments);
        } else {
            LOGGER.debug("Binding named parameters for {}.", method);
            namedParameterBinder.bind(query, method, arguments);
        }
    }

    private boolean isPositionalParameters(final Set<Parameter<?>> parameters) {
        return parameters.iterator().next().getPosition() != null;
    }
}
