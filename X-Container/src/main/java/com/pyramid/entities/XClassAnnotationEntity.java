package com.pyramid.entities;

import com.alibaba.fastjson.JSON;
import com.pyramid.loaders.XClassLoader;

/**
 * @author thunderbolt.lei
 *
 */
public class XClassAnnotationEntity {

    // fieldName-jarName
    private String key;

    // key 对应的类加载器
    private XClassLoader xClassLoader;

    public XClassAnnotationEntity() {
        this.key = null;
        this.xClassLoader = null;
    }

    public XClassAnnotationEntity(String key, XClassLoader xClassLoader) {
        this.key = key;
        this.xClassLoader = xClassLoader;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public XClassLoader getxClassLoader() {
        return xClassLoader;
    }

    public void setxClassLoader(XClassLoader xClassLoader) {
        this.xClassLoader = xClassLoader;
    }

    @Override
    public String toString() {
        return "XClassAnnotationEntity [key=" + key + ", xClassLoader="
                + JSON.toJSONString(xClassLoader) + "]";
    }
}
