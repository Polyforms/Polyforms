package org.polyforms.delegation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that method delegate another method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelegatedBy {

    /**
     * The type of bean which delegator method in.
     */
    Class<?> value();

    /**
     * The name of method which delegates.
     * 
     * Default value is empty string which means same name with annotated method.
     */
    String methodName() default "";

    /**
     * The parameter types of delegator method, which is used when method overload exists.
     * 
     * The specific parameter types are using to find the exactly matching method. If not found, the only method with
     * specific name in {link #methodName()) is an alternative.
     */
    Class<?>[] parameterTypes() default {};
}
