package com.pyramid.testA;

/**
 * @author thunderbolt.lei <br>
 *
 * @description
 *
 */
public class Records {
    // The default record factory
    private static final RecordFactoryImpl factory = RecordFactoryProvider
            .getRecordFactory("com.pyramid.testA.RecordFactoryImpl");

    public static <T> T newRecord(Class<T> cls) {
        return factory.newRecordInstance(cls);
    }
}
