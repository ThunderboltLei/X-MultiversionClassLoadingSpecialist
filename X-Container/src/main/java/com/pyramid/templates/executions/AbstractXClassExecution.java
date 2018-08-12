package com.pyramid.templates.executions;

import com.pyramid.factories.XMavenWorkFactory;
import com.pyramid.factories.XPlexusContainerFactory;
import com.pyramid.pools.XClassPool;

/**
 * @author thunderbolt.lei <br>
 * @param <T>
 *            T is a generic type.
 * 
 *            It's an abstract class which need to be extended to execute all
 *            the maven projects which you want to build.<br>
 *
 */
public abstract class AbstractXClassExecution<T> implements XClassExecution<T> {

    protected XMavenWorkFactory mavenWorkFactory;

    protected XPlexusContainerFactory plexusContainerFactory;
    
    protected XClassPool xClassPool;

    @Override
    public void init() {
        // TODO Auto-generated method stub
        mavenWorkFactory = XMavenWorkFactory.getInstance();
        plexusContainerFactory = XPlexusContainerFactory.getInstance();
        xClassPool = XClassPool.getInstance();
    }

    @Override
    public void execution() {
        // TODO Auto-generated method stub
    }

}
