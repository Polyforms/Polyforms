package org.polyforms.repository.integration;

import javax.persistence.NonUniqueResultException;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GetRepositoryIT extends RepositoryIT {
    @Autowired
    private MockEntityRepository mockEntityRepository;

    @Test
    public void getEntity() {
        Assert.assertNotNull(mockEntityRepository.getByCodeAndName("code1", "name"));
    }

    @Test
    public void getInexistentEntity() {
        Assert.assertNull(mockEntityRepository.getByCodeAndName("InexistentCode", "name"));
    }

    @Test(expected = NonUniqueResultException.class)
    public void getMoreThanOneEntity() {
        mockEntityRepository.getByName("name");
    }
}
