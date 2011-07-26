package org.polyforms.repository.integration;

import org.junit.Assert;

import org.junit.Test;
import org.polyforms.repository.integration.mock.MockEntity;
import org.polyforms.repository.integration.mock.PropertyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public abstract class EntityConverterIT extends RepositoryIT {
    @Autowired
    private ConversionService conversionService;

    @Test
    public void toEntity() {
        Assert.assertEquals("code1", conversionService.convert(1L, MockEntity.class).getCode());
    }

    @Test
    public void inexistentEntity() {
        Assert.assertNull(conversionService.convert(10L, MockEntity.class));
    }

    @Test
    public void nullEntity() {
        Assert.assertNull(conversionService.convert(null, Long.class));
    }

    @Test
    public void FromEntity() {
        Assert.assertEquals(1L, conversionService.convert(new MockEntity(1L), Long.class).longValue());
    }

    @Test
    public void FromPropertyEntity() {
        Assert.assertEquals(1L, conversionService.convert(new PropertyEntity(1L), Long.class).longValue());
    }
}
