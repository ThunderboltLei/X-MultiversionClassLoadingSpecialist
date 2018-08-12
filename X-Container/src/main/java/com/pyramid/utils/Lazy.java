package com.pyramid.utils;

import java.io.File;

/**
 * @author thunderbolt.lei
 *
 */
public class Lazy {

    /**
     * System.getProperty("user.dir") & System.getProperty("basedir")<br>
     * 
     * Difference:
     */
    static {
        final String path = System.getProperty("basedir");
        BASEDIR = null != path ? path : new File("").getAbsolutePath();
    }

    public static final String BASEDIR;

    public static File getFullPath(String subPath) {
        return new File(Lazy.BASEDIR + File.separator + subPath);
    }

}
