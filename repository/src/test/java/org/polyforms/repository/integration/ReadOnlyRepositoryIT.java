package org.polyforms.repository.integration;

import javax.persistence.EntityNotFoundException;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.ReadOnlyRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ReadOnlyRepositoryIT extends RepositoryIT {
    @Autowired
    private ReadOnlyRepository mockEntityRepository;

    @Test
    public void getEntity() {
        Assert.assertNotNull(mockEntityRepository.get(1L));
    }

    @Test
    public void getInexistentEntity() {
        Assert.assertNull(mockEntityRepository.get(10L));
    }

    @Test
    public void loadEntity() {
        Assert.assertNotNull(mockEntityRepository.load(1L));
    }

    @Test(expected = EntityNotFoundException.class)
    public void loadInexistentEntity() {
        mockEntityRepository.load(10L);
    }

    @Test
    public void findAllEntities() {
        Assert.assertEquals(3, mockEntityRepository.findAll().size());
    }

    @Test
    public void findEntities() {
        Assert.assertEquals(2, mockEntityRepository.find(1L, 3L, 5L).size());
    }
}
