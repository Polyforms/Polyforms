package org.polyforms.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that methods do nothing when they are invoked by clients.
 * 
 * The annotated methods are normally used as place holder for other actions like sending events.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoOperation {
}
