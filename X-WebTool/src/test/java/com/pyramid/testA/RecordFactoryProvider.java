package com.pyramid.testA;

import java.lang.reflect.Method;

public class RecordFactoryProvider {

    private RecordFactoryProvider() {
    }

    public static RecordFactoryImpl getRecordFactory(String recordFactoryClassName) {
        return (RecordFactoryImpl) getFactoryClassInstance(recordFactoryClassName);
    }

    private static Object getFactoryClassInstance(String factoryClassName) {
        try {
            Class<?> clazz = Class.forName(factoryClassName);
            Method method = clazz.getMethod("get", null);
            method.setAccessible(true);
            return method.invoke(null, null);
        } catch (Exception e) {
            try {
                throw new Exception(e);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return null;
    }
}
