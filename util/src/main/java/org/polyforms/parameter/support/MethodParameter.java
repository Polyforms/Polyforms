package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.annotation.At;
import org.polyforms.parameter.annotation.Name;
import org.polyforms.parameter.annotation.TypeOf;

public class MethodParameter extends Parameter {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends Annotation>, Action> annotationActions = new HashMap<Class<? extends Annotation>, Action>();
    static {
        annotationActions.put(At.class, new Action<At>() {
            public void apply(final Parameter parameter, final At annotation) {
                parameter.setIndex(annotation.value());
            }
        });
        annotationActions.put(Name.class, new Action<Name>() {
            public void apply(final Parameter parameter, final Name annotation) {
                parameter.setName(annotation.value());
            }
        });
        annotationActions.put(TypeOf.class, new Action<TypeOf>() {
            public void apply(final Parameter parameter, final TypeOf annotation) {
                parameter.setType(annotation.value());
            }
        });
    }

    private Annotation annotation;

    public MethodParameter(final Class<?> type, final int index) {
        super(type, index);
    }

    @SuppressWarnings("unchecked")
    public void setAnnotation(final Annotation annotation, final boolean apply) {
        this.annotation = annotation;
        if (apply) {
            annotationActions.get(annotation.getClass()).apply(this, annotation);
        }
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    private interface Action<A extends Annotation> {
        void apply(Parameter parameter, A annotation);
    }
}
