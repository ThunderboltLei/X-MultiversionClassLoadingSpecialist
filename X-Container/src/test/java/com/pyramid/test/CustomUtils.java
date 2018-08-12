package com.pyramid.test;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author thunderbolt.lei
 *
 */
public class CustomUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CustomUtils.class);

    public static void getInfo(Class<?> clazz) {
        String name = "";
        String gender = "";
        String profile = "";
        Field fields[] = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Name.class)) {
                Name arg0 = field.getAnnotation(Name.class);
                name = name + arg0.value();
                LOG.info("Gmw - name=" + name);
            }
            if (field.isAnnotationPresent(Gender.class)) {
                Gender arg0 = field.getAnnotation(Gender.class);
                gender = gender + arg0.gender().toString();
                LOG.info("Gmw - gender=" + gender);
            }
            if (field.isAnnotationPresent(Profile.class)) {
                Profile arg0 = field.getAnnotation(Profile.class);
                profile = "[id=" + arg0.id() + ",height=" + arg0.height() + ",nativePlace="
                        + arg0.nativePlace() + "]";
                LOG.info("Gmw - profile=" + profile);
            }
        }
    }

}
