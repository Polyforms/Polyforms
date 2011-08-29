package org.polyforms.repository.support;

import java.lang.annotation.Annotation;

import org.polyforms.repository.spi.RepositoryMatcher;

/**
 * Strategy of finding repository inheriting Repository.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class AnnotatedRepositoryMatcher implements RepositoryMatcher {
    private final Class<? extends Annotation> annotationClass;

    public AnnotatedRepositoryMatcher(final Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final Class<?> candidate) {
        return candidate.isAnnotationPresent(annotationClass);
    }
}
