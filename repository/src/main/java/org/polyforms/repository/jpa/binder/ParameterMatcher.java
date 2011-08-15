package org.polyforms.repository.jpa.binder;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.persistence.Parameter;

interface ParameterMatcher<T> {
    Map<T, Integer> match(Method method, Set<Parameter<?>> parameters);
}
