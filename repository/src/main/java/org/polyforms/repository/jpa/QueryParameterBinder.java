package org.polyforms.repository.jpa;

import java.lang.reflect.Method;

import javax.persistence.Query;

public interface QueryParameterBinder {
    void bind(Query query, Method method, Object[] arguments);
}
