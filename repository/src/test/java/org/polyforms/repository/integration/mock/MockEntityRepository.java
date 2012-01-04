package org.polyforms.repository.integration.mock;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public interface MockEntityRepository extends Repository<MockEntity> {
    MockEntity loadByCodeAndName(String code, String name);

    MockEntity loadByName(String name);

    MockEntity getByCodeAndName(String code, String name);

    MockEntity getByName(String name);

    List<MockEntity> findEntitiesByName(String name);

    @Transactional
    void updateNameByCode(String code, String name);

    @Transactional
    void removeByCode(String code);

    void findByQuery(String query, String code);

    long count();
}
