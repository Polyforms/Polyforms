package org.polyforms.repository.integration;

import org.junit.Assert;
import org.junit.Test;
import org.polyforms.repository.integration.mock.QMockEntity;
import org.polyforms.repository.integration.mock.QueryDslRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysema.query.types.expr.BooleanExpression;

public abstract class QueryDslIT extends RepositoryIT {
    @Autowired
    private QueryDslRepository queryDslRepository;

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
