package org.polyforms.repository.integration.mock;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface ReadOnlyRepository extends Repository<MockEntity> {
    MockEntity get(Long identifier);

    MockEntity load(Long identifier);

    List<MockEntity> find(Long... identifiers);

    List<MockEntity> findAll();
}
