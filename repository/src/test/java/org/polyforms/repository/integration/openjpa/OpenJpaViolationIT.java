package org.polyforms.repository.integration.openjpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.RepositoryIT;
import org.polyforms.repository.integration.mock.MockEntity;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("openJpa-context.xml")
public class OpenJpaViolationIT extends RepositoryIT {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void getEntityforNonEntity() {
        // Should throw IllegalArgumentException if not an entity
        Assert.assertNull(entityManager.getMetamodel().entity(Object.class));
    }

    @Test
    public void findEntityByNullIdentifier() {
        // Should throw IllegalArgumentException if identifier is null
        Assert.assertNull(entityManager.find(MockEntity.class, null));
    }
}
