package com.pyramid.loaders;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author thunderbolt.lei
 *
 */
public class XClassLoader extends URLClassLoader {

    private ClassLoader classLoader;

    /**
     * @param urls
     * @param parent
     */
    public XClassLoader(URL[] urls, ClassLoader clazzLoader) {
         super(urls, clazzLoader);
         this.classLoader = clazzLoader;
    }

    @Override
    public void addURL(URL url) {
        // TODO Auto-generated method stub
        super.addURL(url);
    }

    @Override
    public String findLibrary(String libname) {
        // TODO Auto-generated method stub
        return super.findLibrary(libname);
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        super.close();
    }

}
