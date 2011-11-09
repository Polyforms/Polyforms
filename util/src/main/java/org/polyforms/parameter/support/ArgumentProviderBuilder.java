package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.polyforms.parameter.annotation.Provider;
import org.polyforms.parameter.provider.ArgumentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

class ArgumentProviderBuilder {
    protected ArgumentProvider fromAnnotation(final Annotation annotation) {
        final Provider provider = AnnotationUtils.findAnnotation(annotation.getClass(), Provider.class);
        Assert.notNull(provider, "Annotation @Provider must present at the annotation.");

        final Class<?> providerClass = provider.value();
        final Object argument = AnnotationUtils.getValue(annotation);

        final Constructor<?> constructor = ClassUtils
                .getConstructorIfAvailable(providerClass, new Class<?>[] { org.polyforms.util.ClassUtils
                        .resolvePrimitiveWrapperIfNecessary(argument.getClass()) });
        Assert.notNull(constructor, "The provider " + providerClass
                + " must have a constructor with paramater of type " + argument.getClass());

        try {
            return (ArgumentProvider) constructor.newInstance(argument);
        } catch (final Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }

        throw new IllegalStateException("Should never get here");
    }
}
