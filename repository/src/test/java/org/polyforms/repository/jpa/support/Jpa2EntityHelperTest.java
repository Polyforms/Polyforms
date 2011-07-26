package org.polyforms.repository.jpa.support;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

import org.junit.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

public class Jpa2EntityHelperTest {
    private Metamodel metamodel;
    private EntityType<?> entityType;
    private Type<?> idType;
    private SingularAttribute<?, ?> id;

    private EntityManager entityManager;
    private EntityHelper entityHelper;

    @Before
    public void setUp() {
        metamodel = EasyMock.createMock(Metamodel.class);
        entityType = EasyMock.createMock(EntityType.class);
        idType = EasyMock.createMock(Type.class);
        id = EasyMock.createMock(SingularAttribute.class);

        entityHelper = new Jpa2EntityHelper();

        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(entityHelper, "entityManager", entityManager);
    }

    @Test
    public void isEntity() {
        mockIdAttribute();
        EasyMock.replay(entityManager, metamodel, entityType, idType);

        Assert.assertTrue(entityHelper.isEntity(MockEntity.class));
        EasyMock.verify(entityManager, metamodel, entityType, idType);
    }

    @Test
    public void isNotEntity() {
        entityManager.getMetamodel();
        EasyMock.expectLastCall().andReturn(metamodel);
        metamodel.entity(Object.class);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        EasyMock.replay(entityManager, metamodel);

        Assert.assertFalse(entityHelper.isEntity(Object.class));
        EasyMock.verify(entityManager, metamodel);
    }

    @Test
    public void isNotEntityForOpenJpa() {
        entityManager.getMetamodel();
        EasyMock.expectLastCall().andReturn(metamodel);
        metamodel.entity(Object.class);
        EasyMock.expectLastCall().andReturn(null);
        EasyMock.replay(entityManager, metamodel);

        Assert.assertFalse(entityHelper.isEntity(Object.class));
        EasyMock.verify(entityManager, metamodel);
    }

    @Test
    public void getIdentifierName() {
        mockIdAttribute();
        id.getName();
        EasyMock.expectLastCall().andReturn("id").times(2);
        EasyMock.replay(entityManager, metamodel, entityType, idType, id);

        Assert.assertEquals("id", entityHelper.getIdentifierName(MockEntity.class));

        // Just for testing cache.
        Assert.assertEquals("id", entityHelper.getIdentifierName(MockEntity.class));
        EasyMock.verify(entityManager, metamodel, entityType, idType, id);
    }

    @Test
    public void getIdentifierClass() {
        mockIdAttribute();
        id.getJavaType();
        EasyMock.expectLastCall().andReturn(Long.class);
        EasyMock.replay(entityManager, metamodel, entityType, idType, id);

        Assert.assertSame(Long.class, entityHelper.getIdentifierClass(MockEntity.class));
        EasyMock.verify(entityManager, metamodel, entityType, idType, id);
    }

    @Test
    public void getMethodIdentifierClass() throws NoSuchMethodException {
        mockIdAttribute();
        id.getJavaMember();
        EasyMock.expectLastCall().andReturn(ReflectionUtils.findMethod(MockEntity.class, "getId", new Class<?>[0]));
        EasyMock.replay(entityManager, metamodel, entityType, idType, id);

        Assert.assertEquals(1L, entityHelper.getIdentifierValue(new MockEntity()));
        EasyMock.verify(entityManager, metamodel, entityType, idType, id);
    }

    @Test
    public void getFieldIdentifierClass() throws NoSuchFieldException {
        mockIdAttribute();
        id.getJavaMember();
        EasyMock.expectLastCall().andReturn(ReflectionUtils.findField(MockEntity.class, "id"));
        EasyMock.replay(entityManager, metamodel, entityType, idType, id);

        Assert.assertEquals(1L, entityHelper.getIdentifierValue(new MockEntity()));
        EasyMock.verify(entityManager, metamodel, entityType, idType, id);
    }

    private void mockIdAttribute() {
        entityManager.getMetamodel();
        EasyMock.expectLastCall().andReturn(metamodel);
        metamodel.entity(MockEntity.class);
        EasyMock.expectLastCall().andReturn(entityType);
        entityType.getIdType();
        EasyMock.expectLastCall().andReturn(idType);
        idType.getJavaType();
        EasyMock.expectLastCall().andReturn(Long.class);
        entityType.getId(Long.class);
        EasyMock.expectLastCall().andReturn(id);
    }

    public static class MockEntity {
        private final Long id = 1L;

        protected Long getId() {
            return id;
        }
    }
}
