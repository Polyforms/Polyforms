package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.annotation.At;
import org.polyforms.parameter.annotation.Named;
import org.polyforms.parameter.annotation.Provider;
import org.polyforms.parameter.annotation.TypeOf;
import org.polyforms.util.AopUtils;
import org.springframework.util.Assert;

/**
 * {@link Parameter} of {@link Method}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class MethodParameter extends Parameter {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends Annotation>, Action> ANNOTATION_ACTIONS = new HashMap<Class<? extends Annotation>, Action>();
    private Annotation annotation;

    static {
        ANNOTATION_ACTIONS.put(At.class, new Action<At>() {
            protected void apply(final Parameter parameter, final At annotation) {
                parameter.setIndex(annotation.value());
            }
        });
        ANNOTATION_ACTIONS.put(Named.class, new Action<Named>() {
            protected void apply(final Parameter parameter, final Named annotation) {
                parameter.setName(annotation.value());
            }
        });
        ANNOTATION_ACTIONS.put(TypeOf.class, new Action<TypeOf>() {
            protected void apply(final Parameter parameter, final TypeOf annotation) {
                parameter.setType(annotation.value());
            }
        });
    }

    /**
     * Set provider annotation of parameter.
     * 
     * @param annotation annotated with {@link Provider}
     * @param apply whether apply the annotation to meta data of parameter.
     */
    @SuppressWarnings("unchecked")
    public void setAnnotation(final Annotation annotation, final boolean apply) {
        this.annotation = annotation;

        if (annotation != null) {
            final Class<?> annotationClass = findAnnotationClass(annotation);
            Assert.notNull(annotationClass, "The annotation for argument provider should be annotated by @Provider.");

            if (apply && ANNOTATION_ACTIONS.containsKey(annotationClass)) {
                ANNOTATION_ACTIONS.get(annotationClass).apply(this, annotation);
            }
        }
    }

    private Class<?> findAnnotationClass(final Annotation annotation) {
        final Class<?>[] candidates = AopUtils.deproxy(annotation.getClass());
        for (final Class<?> candidate : candidates) {
            if (candidate.isAnnotationPresent(Provider.class)) {
                return candidate;
            }
        }
        return null;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    private abstract static class Action<A extends Annotation> {
        protected abstract void apply(Parameter parameter, A annotation);
    }
}
