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

public class MethodParameter extends Parameter {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends Annotation>, Action> ANNOTATION_ACTIONS = new HashMap<Class<? extends Annotation>, Action>();
    private Annotation annotation;

    static {
        ANNOTATION_ACTIONS.put(At.class, new Action<At>() {
            public void apply(final Parameter parameter, final At annotation) {
                parameter.setIndex(annotation.value());
            }
        });
        ANNOTATION_ACTIONS.put(Named.class, new Action<Named>() {
            public void apply(final Parameter parameter, final Named annotation) {
                parameter.setName(annotation.value());
            }
        });
        ANNOTATION_ACTIONS.put(TypeOf.class, new Action<TypeOf>() {
            public void apply(final Parameter parameter, final TypeOf annotation) {
                parameter.setType(annotation.value());
            }
        });
    }

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

    private interface Action<A extends Annotation> {
        void apply(Parameter parameter, A annotation);
    }
}
