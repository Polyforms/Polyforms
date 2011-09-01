package org.polyforms.repository.support;

import java.lang.annotation.Annotation;

import org.polyforms.repository.spi.RepositoryMatcher;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/**
 * Strategy of finding Repository by annotation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class AnnotatedRepositoryMatcher implements RepositoryMatcher {
    private final Class<? extends Annotation> annotationClass;

    /**
     * Create an instance with annotation identifies Repository.
     */
    public AnnotatedRepositoryMatcher(final Class<? extends Annotation> annotationClass) {
        Assert.notNull(annotationClass);
        this.annotationClass = annotationClass;
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final Class<?> candidate) {
        return AnnotationUtils.findAnnotation(candidate, annotationClass) != null;
    }
}
