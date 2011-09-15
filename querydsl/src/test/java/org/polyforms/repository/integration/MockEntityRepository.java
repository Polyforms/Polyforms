package org.polyforms.repository.integration;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mysema.query.types.Predicate;

@Component
public interface MockEntityRepository extends Repository<MockEntity> {
}

interface Repository<T> {
    T get(Predicate predicate);

    List<T> find(Predicate predicate);

    long count(Predicate predicate);
}
