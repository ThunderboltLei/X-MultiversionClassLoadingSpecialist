package com.pyramid.templates.constructions;

/**
 * @author thunderbolt.lei <br>
 *
 */
public interface XClassConstruction<T> {

    public void init(Class<T> clazz);

    /**
     * @param container
     */
    public void construct();

}
