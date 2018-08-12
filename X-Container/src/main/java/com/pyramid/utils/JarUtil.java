package com.pyramid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * @author thunderbolt.lei
 *
 */
public class JarUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JarUtil.class);
    private static final String JAR_SUFFIX = ".jar";

    /**
     * To get all jars in a specified directory.
     * 
     * @param subPath
     *            a relative path.
     * @return
     */
    public static List<File> getAllJars(String subPath) {
        List<File> jarList = Lists.newArrayList();
        recursivePath(Lazy.getFullPath(subPath).getAbsolutePath(), jarList);
        return jarList;
    }

    /**
     * To recurse to find some jar files.
     * 
     * @param rootPath
     *            an absolute path.
     */
    protected static void recursivePath(String rootPath, List<File> jarList) {
        File root = new File(rootPath);
        for (File child : root.listFiles()) {
            if (child.isDirectory()) {
                recursivePath(child.getPath(), jarList);
            } else {
                if (child.getPath().endsWith(JAR_SUFFIX)) {
                    jarList.add(child);
                }
            }
        }
    }

    /**
     * 
     * @param jarPath
     * @return
     * @throws Exception
     */
    public static List<String> parse(String jarPath) throws Exception {
        File f = new File(jarPath);
        // URL url = new URL("jar:file://" + f.getAbsolutePath() + "!/");
        // System.out.println(url.getPath());

        List<String> list = new ArrayList<String>();
        JarFile jar = new JarFile(f);
        jar.stream().forEach(x -> {
            String child = x.getName();
            if (!child.startsWith("META-INF")) {
                list.add(child);
            } else if (!child.startsWith("LICENSE")) {
                list.add(child);
            } else if (!child.startsWith("NOTICE")) {
                list.add(child);
            }
        });

        return list;
    }

    /**
     * To copy one jar file to a directory.<br>
     * 
     * @param jarFile
     *            the source jar file
     * @param destDir
     *            the destination directory
     */
    public static void copyFiletoDirectory(File sourceFile, File destDir) {
        if (!sourceFile.exists()) {
            try {
                throw new Exception("Exception: Jar file does not exist.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.copyFileToDirectory(sourceFile, destDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        String subPath = "libs/kafka/0.11.0.3";
        List<File> jarList = JarUtil.getAllJars(subPath);
        jarList.forEach(x -> {
            LOG.info("--->>> {}", x.getName());
        });

        // String jarPath =
        // "/opt/developments/eclipse-oxygen3a-j2ee-work/myprojects/thunderboltlei/X-Project/java/X-LoadingClassExpert/libs/kafka/0.11.0.3/kafka-clients-0.11.0.3.jar";
        // parse(jarPath);
    }

}
