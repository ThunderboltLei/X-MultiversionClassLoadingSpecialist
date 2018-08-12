package com.pyramid.pools;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;

/**
 * @author thunderbolt.lei <br>
 * @param <T>
 *
 */
public class XClassPoolFactory<T> extends BasePooledObjectFactory<T> {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.pool2.BasePooledObjectFactory#create()
     */
    @Override
    public T create() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.pool2.BasePooledObjectFactory#wrap(java.lang.Object)
     */
    @Override
    public PooledObject<T> wrap(T obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PooledObject<T> makeObject() throws Exception {
        // TODO Auto-generated method stub
        return super.makeObject();
    }

    @Override
    public void destroyObject(PooledObject<T> p) throws Exception {
        // TODO Auto-generated method stub
        super.destroyObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<T> p) {
        // TODO Auto-generated method stub
        return super.validateObject(p);
    }

    @Override
    public void activateObject(PooledObject<T> p) throws Exception {
        // TODO Auto-generated method stub
        super.activateObject(p);
    }

    @Override
    public void passivateObject(PooledObject<T> p) throws Exception {
        // TODO Auto-generated method stub
        super.passivateObject(p);
    }

}
