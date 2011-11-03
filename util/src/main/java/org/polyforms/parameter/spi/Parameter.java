package org.polyforms.parameter.spi;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.polyforms.parameter.annotation.At;
import org.polyforms.parameter.annotation.Name;
import org.polyforms.parameter.annotation.TypeOf;

public class Parameter {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends Annotation>, Action> annotationActions = new HashMap<Class<? extends Annotation>, Action>();
    static {
        annotationActions.put(At.class, new Action<At>() {
            public void apply(Parameter parameter, At annotation) {
                parameter.index = annotation.value();
            }
        });
        annotationActions.put(Name.class, new Action<Name>() {
            public void apply(Parameter parameter, Name annotation) {
                parameter.name = annotation.value();
            }
        });
        annotationActions.put(TypeOf.class, new Action<TypeOf>() {
            public void apply(Parameter parameter, TypeOf annotation) {
                parameter.type = annotation.value();
            }
        });
    }

    private Class<?> type;
    private int index;
    private String name;

    public Parameter(Class<?> type, int index) {
        this.type = type;
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public void applyAnnotation(Annotation annotation) {
        annotationActions.get(annotation.getClass()).apply(this, annotation);
    }

    public Class<?> getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Parameter [type=" + type + ", index=" + index + ", name=" + name + "]";
    }

    private interface Action<A extends Annotation> {
        void apply(Parameter parameter, A annotation);
    }
}
