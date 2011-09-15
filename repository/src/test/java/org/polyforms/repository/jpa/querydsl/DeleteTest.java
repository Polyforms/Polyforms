package org.polyforms.repository.jpa.querydsl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.spi.EntityClassResolver;
import org.springframework.test.util.ReflectionTestUtils;

public class DeleteTest {
    private QueryDslExecutor executor;
    private EntityManager entityManger;

    @Before
    public void setUp() {
        executor = new Delete(EasyMock.createMock(EntityClassResolver.class));
        entityManger = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(executor, "entityManager", entityManger);
    }

    @Test
    public void get() {
        final Query query = EasyMock.createMock(Query.class);

        entityManger.createQuery("delete from MockEntity mockEntity\nwhere mockEntity.code = :a1");
        EasyMock.expectLastCall().andReturn(query);
        query.setParameter("a1", "code1");
        EasyMock.expectLastCall().andReturn(query);
        query.executeUpdate();
        EasyMock.expectLastCall().andReturn(2L);
        EasyMock.replay(entityManger, query);

        Assert.assertSame(2L, executor.getResult(QMockEntity.mockEntity, null, QMockEntity.mockEntity.code.eq("code1")));
        EasyMock.verify(entityManger, query);
    }
}
