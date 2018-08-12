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
public @interface Gender {

    public enum GenderType {

        Male("男"), Female("女"), Other("中性");

        private String genderStr;

        private GenderType(String arg0) {
            this.genderStr = arg0;
        }

        @Override
        public String toString() {
            return genderStr;
        }
    }

    GenderType gender() default GenderType.Male;

}
