package com.pyramid.testA;

/**
 * @author thunderbolt.lei <br>
 *
 * @description
 *
 */
public interface RecordFactory {
    public <T> T newRecordInstance(Class<T> clazz);
}
