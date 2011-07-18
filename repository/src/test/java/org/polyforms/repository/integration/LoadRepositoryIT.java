package org.polyforms.repository.integration;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class LoadRepositoryIT extends RepositoryIT {
    @Autowired
    private MockEntityRepository mockEntityRepository;

    @Test
    public void getEntity() {
        Assert.assertNotNull(mockEntityRepository.loadByCodeAndName("code1", "name"));
    }

    @Test(expected = NoResultException.class)
    public void getInexistentEntity() {
        mockEntityRepository.loadByCodeAndName("InexistentCode", "name");
    }

    @Test(expected = NonUniqueResultException.class)
    public void getMoreThanOneEntity() {
        mockEntityRepository.loadByName("name");
    }
}
