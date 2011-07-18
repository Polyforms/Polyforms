package org.polyforms.repository.jpa;

import java.lang.reflect.Method;

import javax.persistence.Query;

public interface QueryBuilder {
    Query build(Method method);
}
