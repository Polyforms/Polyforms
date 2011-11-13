package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.parameter.annotation.At;
import org.polyforms.parameter.annotation.Constant;
import org.polyforms.parameter.annotation.Named;
import org.polyforms.parameter.annotation.TypeOf;

public class MethodParameterTest {
    private MethodParameter methodParameter;
    private Annotation[][] annotations;

    @Before
    public void setUp() throws NoSuchMethodException {
        methodParameter = new MethodParameter();
        final Method method = this.getClass().getMethod("annotatedMethod",
                new Class<?>[] { String.class, String.class, String.class, String.class });
        annotations = method.getParameterAnnotations();
    }

    @Test
    public void setAnnotation() {
        final Annotation annotation = annotations[0][1];
        methodParameter.setAnnotation(annotation);
        Assert.assertSame(annotation, methodParameter.getAnnotation());
    }

    @Test
    public void setNullAnnotation() {
        methodParameter.setAnnotation(null);
        methodParameter.applyAnnotation();
        Assert.assertNull(methodParameter.getAnnotation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notProvider() {
        methodParameter.setAnnotation(annotations[0][0]);
    }

    @Test
    public void applyAt() {
        final Annotation annotation = annotations[1][0];
        methodParameter.setAnnotation(annotation);
        methodParameter.applyAnnotation();
        Assert.assertEquals(0, methodParameter.getIndex());
    }

    @Test
    public void applyNamed() {
        final Annotation annotation = annotations[2][0];
        methodParameter.setAnnotation(annotation);
        methodParameter.applyAnnotation();
        Assert.assertEquals("named", methodParameter.getName());
    }

    @Test
    public void applyTypeOf() {
        final Annotation annotation = annotations[3][0];
        methodParameter.setAnnotation(annotation);
        methodParameter.applyAnnotation();
        Assert.assertEquals(String.class, methodParameter.getType());
    }

    public void annotatedMethod(@Deprecated @Constant("constant") final String constant, @At(0) final String at,
            @Named("named") final String named, @TypeOf(String.class) final String typeOf) {
    }
}
