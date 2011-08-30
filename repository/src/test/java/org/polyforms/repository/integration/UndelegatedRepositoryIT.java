package org.polyforms.repository.integration;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntity;
import org.polyforms.repository.integration.mock.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class UndelegatedRepositoryIT extends RepositoryIT {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OverridedRepository overridedRepository;

    @Autowired
    private UnsupportedRepository unsupportedRepository;

    @Test
    public void removeEntity() {
        overridedRepository.remove(entityManager.find(MockEntity.class, 1L));
        Assert.assertNotNull(entityManager.find(MockEntity.class, 1L));
    }

    @Test
    public void executorCachedMethod() {
        unsupportedRepository.save(new MockEntity(10L));
        Assert.assertNotNull(entityManager.find(MockEntity.class, 10L));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportMethod() {
        unsupportedRepository.unsupportedMethod();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void uncompletedMethodName() {
        unsupportedRepository.count();
    }

    @Test(expected = IllegalArgumentException.class)
    public void inExistentQuery() {
        unsupportedRepository.findByNothing();
    }
}

interface OverridedRepository extends Repository<MockEntity> {
    void remove(final MockEntity... entities);
}

@Component
class OverridedRepositoryImpl implements OverridedRepository {
    public void save(final MockEntity... entities) {
        // do nothing
    }

    public void remove(final MockEntity... entities) {
        // do nothing
    }
}

@Component
interface UnsupportedRepository extends Repository<MockEntity> {
    void save(final MockEntity... entities);

    void unsupportedMethod();

    List<MockEntity> findByNothing();

    long count();
}

@Component
interface NonRepository {
    String echo();
}
