package org.polyforms.parameter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.polyforms.parameter.provider.ArgumentOfType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Provider(ArgumentOfType.class)
public @interface TypeOf {
    Class<?> value();
}
