package org.polyforms.repository.integration.mock;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mysema.query.types.Predicate;

@Component
public interface QueryDslRepository extends Repository<MockEntity> {
    MockEntity get(Predicate predicate);

    List<MockEntity> find(Predicate predicate);

    long count(Predicate predicate);
}
