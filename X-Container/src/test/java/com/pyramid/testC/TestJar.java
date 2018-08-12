package com.pyramid.testC;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pyramid.builder.XClassBuilder;
import com.pyramid.loaders.XClassLoader;
import com.pyramid.utils.JarUtil;

/**
 * @author thunderbolt.lei <br>
 *
 */
public class TestJar {

    private static final Logger LOG = LoggerFactory.getLogger(TestJar.class);

    private void test01() {
        String subPath = "libs/kafka/0.11.0.3";
        List<File> jarList = JarUtil.getAllJars(subPath);
        XClassLoader xLoader = new XClassBuilder().loadJars(jarList);
    }

    public static void main(String[] args) {

        TestJar test = new TestJar();

        test.test01();

    }

}
