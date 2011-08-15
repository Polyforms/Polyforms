package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.Set;

import javax.persistence.Parameter;
import javax.persistence.Query;

interface ParameterBinder<T> {
    void bind(final Query query, final Method method, final Set<Parameter<?>> parameters, final Object[] arguments);
}
