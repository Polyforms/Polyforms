package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;
import org.polyforms.parameter.annotation.At;
import org.polyforms.parameter.provider.ArgumentAt;
import org.polyforms.parameter.provider.ArgumentProvider;

public class ArgumentProviderBuilderTest {
    private final ArgumentProviderBuilder argumentProviderBuilde = new ArgumentProviderBuilder();

    @Test
    public void build() throws NoSuchMethodException {
        final Method method = this.getClass().getMethod("at", new Class<?>[] { String.class });
        final Annotation[][] annotations = method.getParameterAnnotations();

        final ArgumentProvider argumentProvider = argumentProviderBuilde.fromAnnotation(annotations[0][0]);
        Assert.assertEquals(ArgumentAt.class, argumentProvider.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noProvider() throws NoSuchMethodException {
        final Method method = this.getClass().getMethod("noProvider", new Class<?>[] { String.class });
        final Annotation[][] annotations = method.getParameterAnnotations();
        argumentProviderBuilde.fromAnnotation(annotations[0][0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noConstructor() throws NoSuchMethodException {
        final Method method = this.getClass().getMethod("noConstructor", new Class<?>[] { String.class });
        final Annotation[][] annotations = method.getParameterAnnotations();
        argumentProviderBuilde.fromAnnotation(annotations[0][0]);
    }

    @Test(expected = RuntimeException.class)
    public void exceptionalConstructor() throws NoSuchMethodException {
        final Method method = this.getClass().getMethod("exceptionalConstructor", new Class<?>[] { String.class });
        final Annotation[][] annotations = method.getParameterAnnotations();
        argumentProviderBuilde.fromAnnotation(annotations[0][0]);
    }

    public void at(@At(0) final String string) {
    }

    public void noProvider(@Deprecated final String string) {
    }

    public void noConstructor(@NoConstructor(0) final String string) {
    }

    public void exceptionalConstructor(@ExceptionalConstructor("") final String string) {
    }
}

class NoConstructorProvider implements ArgumentProvider {
    public NoConstructorProvider(final String string) {
        throw new RuntimeException();
    }

    public void validate(final Method method) {
    }

    public Object get(final Object... arguments) {
        return null;
    }
}
