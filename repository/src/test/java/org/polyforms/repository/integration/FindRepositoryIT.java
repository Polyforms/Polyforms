package org.polyforms.repository.integration;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FindRepositoryIT extends RepositoryIT {
    @Autowired
    private MockEntityRepository mockEntityRepository;

    @Test
    public void findEntities() {
        Assert.assertEquals(2, mockEntityRepository.findEntitiesByName("name").size());
    }

    @Test
    public void countEntities() {
        Assert.assertEquals(3, mockEntityRepository.count());
    }
}
