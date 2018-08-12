package com.pyramid.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
/**
 * @author thunderbolt.lei
 *
 */
public @interface XJarAnnotation {

    String jarPath() default ".";

    String jarName() default "demo";

    String version() default "0.0.1";

}
