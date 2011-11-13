package org.polyforms.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a method publishing an event before and/or after method invocation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Publisher {
    /**
     * Name of event.
     */
    String value();

    /**
     * The time to publish the event.
     */
    When when() default When.AFTER;

    /**
     * Time to publish event.
     */
    enum When {
        BEFORE, AFTER
    }
}
