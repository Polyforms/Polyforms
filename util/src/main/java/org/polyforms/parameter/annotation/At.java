package org.polyforms.parameter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.polyforms.parameter.provider.ArgumentAt;

/**
 * Annotation to locate argument by index provided.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Provider(ArgumentAt.class)
public @interface At {
    /**
     * Index of matched parameter.
     */
    int value();
}
