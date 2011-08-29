package org.polyforms.repository.integration.mock;

import org.springframework.stereotype.Component;

@Component
public interface UpdatableRepository extends Repository<MockEntity> {
    void save(MockEntity... entities);

    void remove(MockEntity... entities);
}
