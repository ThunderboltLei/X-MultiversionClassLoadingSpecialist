package com.pyramid.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * @author thunderbolt.lei
 *
 */
public @interface Profile {
    /**
     * ID
     *
     * @return
     */
    public int id() default -1;

    /**
     * 身高
     *
     * @return
     */
    public int height() default 0;

    /**
     * 籍贯
     *
     * @return
     */
    public String nativePlace() default "";
}
