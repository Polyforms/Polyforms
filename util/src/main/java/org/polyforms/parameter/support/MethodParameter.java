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
    private Annotation annotation;

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

    @SuppressWarnings("unchecked")
    public void setAnnotation(final Annotation annotation, final boolean apply) {
        this.annotation = annotation;
        if (apply && annotation != null) {
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
