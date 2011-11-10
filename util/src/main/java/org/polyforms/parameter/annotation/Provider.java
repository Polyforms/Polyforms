package org.polyforms.parameter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.polyforms.parameter.ArgumentProvider;

/**
 * Annotation to identify annotation of argument provider.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Provider {
    /**
     * Class of {@link ArgumentProvider} converted to.
     */
    Class<? extends ArgumentProvider> value();
}
