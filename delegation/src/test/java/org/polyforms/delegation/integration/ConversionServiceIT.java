package org.polyforms.delegation.integration;

import java.lang.annotation.ElementType;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("ComponentScannerIT-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public final class ConversionServiceIT {
    @Autowired
    private ConversionService conversionService;

    @Test
    public void convertString() {
        Assert.assertEquals("1", conversionService.convert(1, String.class));
    }

    @Test
    public void convertEnum() {
        Assert.assertEquals(ElementType.METHOD, conversionService.convert("METHOD", ElementType.class));
    }

    @Test
    public void convertNull() {
        Assert.assertNull(conversionService.convert(null, MockEntity.class));
    }

    @Test
    public void convertSameType() {
        final boolean value = true;
        Assert.assertTrue(conversionService.convert(value, boolean.class));
    }

    @Test
    public void convertBean() {
        final MockEntity mockEntity = conversionService.convert(newMockDto(), MockEntity.class);
        Assert.assertEquals(1, mockEntity.getId().intValue());
        Assert.assertEquals("mock", mockEntity.getName());
        Assert.assertEquals(ElementType.METHOD, mockEntity.getType());
        final List<String> children = mockEntity.getChildren();
        Assert.assertEquals("10", children.get(0));
        Assert.assertEquals("20", children.get(1));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void convertCollection() throws SecurityException, NoSuchMethodException {
        final MockDto[] source = new MockDto[] { newMockDto() };
        final MockEntity mockEntity = ((List<MockEntity>) conversionService.convert(source, TypeDescriptor
                .forObject(source),
                new TypeDescriptor(new MethodParameter(this.getClass().getDeclaredMethod("getEntities"), -1)))).get(0);
        Assert.assertEquals(1, mockEntity.getId().intValue());
    }

    protected List<MockEntity> getEntities() {
        return null;
    }

    private MockDto newMockDto() {
        final MockDto mockDto = new MockDto();
        mockDto.setId("1");
        mockDto.setName("mock");
        mockDto.setType("METHOD");
        mockDto.setChildren(new Integer[] { 10, 20 });
        return mockDto;
    }

    public static final class MockDto {
        private String id;
        private String name;
        private String type;
        private Integer[] children;

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        public Integer[] getChildren() {
            return children;
        }

        public void setChildren(final Integer[] children) {
            this.children = children;
        }
    }

    public static final class MockEntity {
        private int id;
        private String name;
        private ElementType type;
        private List<String> children;

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public ElementType getType() {
            return type;
        }

        public List<String> getChildren() {
            return children;
        }
    }
}
