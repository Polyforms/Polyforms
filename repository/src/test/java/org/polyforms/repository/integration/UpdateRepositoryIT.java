package org.polyforms.repository.integration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntity;
import org.polyforms.repository.integration.mock.MockEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UpdateRepositoryIT extends RepositoryIT {
    @Autowired
    private MockEntityRepository mockEntityRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void updateEntity() {
        mockEntityRepository.updateNameByCode("code1", "newName");
        Assert.assertEquals("newName", entityManager.find(MockEntity.class, 1L).getName());
    }

    @Test
    public void removeEntity() {
        mockEntityRepository.removeByCode("code1");
        Assert.assertNull(entityManager.find(MockEntity.class, 1L));
    }
}
