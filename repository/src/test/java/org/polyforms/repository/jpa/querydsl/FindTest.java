package org.polyforms.repository.jpa.querydsl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.EntityClassResolver;
import org.springframework.test.util.ReflectionTestUtils;

import com.mysema.query.types.Predicate;

public class FindTest {
    private QueryDslExecutor executor;
    private EntityManager entityManger;

    @Before
    public void setUp() {
        executor = new Find(EasyMock.createMock(EntityClassResolver.class));
        entityManger = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(executor, "entityManager", entityManger);
    }

    @Test
    public void get() throws NoSuchMethodException {
        final MockEntity mockEntity = new MockEntity();
        final Query query = EasyMock.createMock(Query.class);

        entityManger.createQuery("select mockEntity\nfrom MockEntity mockEntity\nwhere mockEntity.code = :a1");
        EasyMock.expectLastCall().andReturn(query);
        query.setParameter("a1", "code1");
        EasyMock.expectLastCall().andReturn(query);
        query.getSingleResult();
        EasyMock.expectLastCall().andReturn(mockEntity);
        EasyMock.replay(entityManger, query);

        Assert.assertSame(mockEntity, executor.getResult(QMockEntity.mockEntity,
                MockRepository.class.getMethod("get", new Class<?>[] { Predicate.class }),
                QMockEntity.mockEntity.code.eq("code1")));
        EasyMock.verify(entityManger, query);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void find() throws NoSuchMethodException {
        final MockEntity mockEntity = new MockEntity();
        final Query query = EasyMock.createMock(Query.class);

        entityManger.createQuery("select mockEntity\nfrom MockEntity mockEntity\nwhere mockEntity.code = :a1");
        EasyMock.expectLastCall().andReturn(query);
        query.setParameter("a1", "code1");
        EasyMock.expectLastCall().andReturn(query);
        query.getResultList();
        EasyMock.expectLastCall().andReturn(Collections.singletonList(mockEntity));
        EasyMock.replay(entityManger, query);

        final List<MockEntity> entities = (List<MockEntity>) executor.getResult(QMockEntity.mockEntity,
                MockRepository.class.getMethod("find", new Class<?>[] { Predicate.class }),
                QMockEntity.mockEntity.code.eq("code1"));
        Assert.assertEquals(1, entities.size());
        Assert.assertSame(mockEntity, entities.get(0));
        EasyMock.verify(entityManger, query);
    }

    private interface MockRepository {
        MockEntity get(Predicate predicate);

        List<MockEntity> find(Predicate predicate);
    }
}
