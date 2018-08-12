package com.pyramid.templates.constructions;

import javax.annotation.PostConstruct;

import com.pyramid.builder.XClassBuilder;
import com.pyramid.pools.XClassPool;

/**
 * @author thunderbolt.lei <br>
 * 
 *         It's an abstract class which need to be extended to construct all the
 *         maven projects which you want to build. <br>
 *
 */
public abstract class AbstractXClassConstruction<T> implements XClassConstruction<T> {

    protected XClassBuilder xClassBuilder;

    protected XClassPool xClassPool;

    @Override
    public void init(Class<T> clazz) {
        // TODO Auto-generated method stub
        xClassBuilder = new XClassBuilder(clazz);
        xClassPool = XClassPool.getInstance();
    }

    @PostConstruct
    @Override
    public void construct() {
        // TODO Auto-generated method stub

    }

}
