package org.polyforms.repository.integration;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public abstract class CustomizedRepositoryIT extends RepositoryIT {
    @Autowired
    private CustomizedRepository customizedRepository;

    @Autowired
    private NoEntityRepository<Object> noEntityRepository;

    @Test
    public void customizeRepository() {
        Assert.assertEquals(2, customizedRepository.findByName("name").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noEntityRepository() {
        noEntityRepository.get("name");
    }
}

@Component
interface NoEntityRepository<T> extends EntityRepository<T> {
    T get(String string);
}

@Component
interface CustomizedRepository extends EntityRepository<MockEntity> {
    List<MockEntity> findByName(String name);
}

interface EntityRepository<T> {
}
