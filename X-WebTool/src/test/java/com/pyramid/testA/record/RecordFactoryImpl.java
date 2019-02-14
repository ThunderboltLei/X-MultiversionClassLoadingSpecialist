package com.pyramid.testA.record;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author thunderbolt.lei <br>
 *
 * @description
 *
 */
public class RecordFactoryImpl implements RecordFactory {

    private static final RecordFactoryImpl self = new RecordFactoryImpl();

    public static RecordFactoryImpl get() {
        return self;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.pyramid.testA.RecordFactory#newRecordInstance(java.lang.Class)
     */
    @Override
    public <T> T newRecordInstance(Class<T> clazz) {
        // TODO Auto-generated method stub
        try {
            Constructor constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
