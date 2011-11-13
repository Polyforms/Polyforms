package org.polyforms.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to annotating multiple {@link Publisher}s.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Publishers {
    /**
     * publishers.
     */
    Publisher[] value();
}
