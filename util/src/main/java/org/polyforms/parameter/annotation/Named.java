package org.polyforms.parameter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.polyforms.parameter.provider.ArgumentNamed;

/**
 * Annotation to locate parameter by name provided.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Provider(ArgumentNamed.class)
public @interface Named {
    /**
     * Name of matched parameter.
     */
    String value();
}
