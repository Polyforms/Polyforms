package org.polyforms.repository.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.expr.BooleanExpression;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class QueryDslIT {
    @Autowired
    private MockEntityRepository queryDslRepository;

    @Test
    public void get() {
        final QMockEntity mockEntity = QMockEntity.mockEntity;
        final BooleanExpression codeEquals = mockEntity.code.eq("code1");
        Assert.assertNotNull(queryDslRepository.get(codeEquals));
    }

    @Test
    public void find() {
        final QMockEntity mockEntity = QMockEntity.mockEntity;
        final BooleanExpression nameEquals = mockEntity.name.eq("name");
        Assert.assertEquals(2, queryDslRepository.find(nameEquals).size());
    }

    @Test
    public void count() {
        final QMockEntity mockEntity = QMockEntity.mockEntity;
        final BooleanExpression nameEquals = mockEntity.name.eq("name");
        Assert.assertEquals(2, queryDslRepository.count(nameEquals));
    }
}
