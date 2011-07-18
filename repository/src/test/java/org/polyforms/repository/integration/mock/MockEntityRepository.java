package org.polyforms.repository.integration.mock;

import java.util.List;

import org.polyforms.repository.ReadOnlyRepository;
import org.polyforms.repository.UpdatableRepository;
import org.springframework.stereotype.Component;

@Component
public interface MockEntityRepository extends ReadOnlyRepository<MockEntity, Long>, UpdatableRepository<MockEntity> {
    MockEntity loadByCodeAndName(String code, String name);

    MockEntity loadByName(String name);

    MockEntity getByCodeAndName(String code, String name);

    MockEntity getByName(String name);

    List<MockEntity> findByName(String name);

    void updateNameByCode(String code, String name);

    void removeByCode(String code);

    void findByQuery(String query, String code);
}
