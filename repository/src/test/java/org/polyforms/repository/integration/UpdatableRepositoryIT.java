package org.polyforms.repository.integration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntity;
import org.polyforms.repository.integration.mock.UpdatableRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UpdatableRepositoryIT extends RepositoryIT {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UpdatableRepository mockEntityRepository;

    @Test
    public void addEntity() {
        mockEntityRepository.save(new MockEntity(10L));
        Assert.assertNotNull(entityManager.find(MockEntity.class, 10L));
    }

    @Test
    public void removeEntity() {
        mockEntityRepository.remove(entityManager.find(MockEntity.class, 1L));
        entityManager.flush();
        Assert.assertNull(entityManager.find(MockEntity.class, 1L));
    }

    @Test
    public void removeAllEntities() {
        mockEntityRepository.remove(entityManager.find(MockEntity.class, 2L), entityManager.find(MockEntity.class, 3L));
        entityManager.flush();
        Assert.assertNull(entityManager.find(MockEntity.class, 2L));
        Assert.assertNull(entityManager.find(MockEntity.class, 3L));
    }
}
