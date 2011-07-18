package org.polyforms.repository.integration;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public abstract class PaginationIT extends RepositoryIT {
    @Autowired
    private MockEntityRepository mockEntityRepository;

    @Test
    public void findEntities() {
        Assert.assertEquals(1, mockEntityRepository.findByName("name").size());
    }
}
