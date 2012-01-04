package org.polyforms.repository.integration.mock;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public interface UpdatableRepository extends Repository<MockEntity> {
    void save(MockEntity... entities);

    void remove(MockEntity... entities);
}
