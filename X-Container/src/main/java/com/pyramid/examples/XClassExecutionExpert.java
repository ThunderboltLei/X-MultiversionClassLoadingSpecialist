package com.pyramid.examples;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.pyramid.entities.XRepoEntity;
import com.pyramid.entities.XYamlEntity;
import com.pyramid.templates.executions.AbstractXClassExecution;
import com.pyramid.utils.JarUtil;
import com.pyramid.utils.Lazy;

/**
 * @author thunderbolt.lei
 *
 */
public class XClassExecutionExpert extends AbstractXClassExecution {

    private static final Logger LOG = LoggerFactory.getLogger(XClassExecutionExpert.class);

    private static final boolean JSON_FORMAT_FLAG = false;

    private String subPath = "libs/kafka/0.11.0.3", //
            pomPath = subPath + File.separator + "pom.xml"; //
    private File pomFile = Lazy.getFullPath(pomPath);
    private File yamlFile = Lazy.getFullPath("src/main/resources/x-main.yaml");

    @Override
    public void init() {
        // TODO Auto-generated method stub
        super.init();
    }

    @Override
    public void execution() {

        xClassPool.executeTask(new FutureTask<Object>(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                // TODO Auto-generated method stub
                Long threadID = Thread.currentThread().getId();
                System.out.println("--->>> " + threadID);
                return threadID;
            }

        }));

        try {
            LOG.info("Test --->>> Start execution", JSON_FORMAT_FLAG);
            Yaml yaml = new Yaml();
            FileInputStream fis = new FileInputStream(yamlFile);
            XYamlEntity yamlEntity = yaml.loadAs(fis, XYamlEntity.class);

            XRepoEntity repo = yamlEntity.getLocalRepo();
            URL url = new URL(repo.getUrl());
            File localRepoFile = new File(url.getPath());
            if (!localRepoFile.exists()) {
                throw new Exception("Exception: the local repository does not exist.");
            }

            MavenExecutionRequest mavenExecutionRequest = mavenWorkFactory
                    .createMavenExecutionRequest(pomFile, yamlEntity);

            // Maven Execution Result
            MavenExecutionResult mavenExecResult = mavenWorkFactory
                    .doXExecution(mavenExecutionRequest);
            if (null == mavenExecResult) {
                throw new Exception("Maven Project executes failed.");
            }

            // All artifacts which are needed as building the maven project.
            List<Artifact> artifactList = Lists
                    .newArrayList(mavenExecResult.getProject().getArtifacts());
            LOG.info("Execution Successful --->>> \nartifactList:{}\nsize:{}\n", JSON_FORMAT_FLAG,
                    JSON.toJSONString(artifactList), artifactList.size());
            artifactList.forEach(x -> {
                LOG.info("--->>> {}", JSON_FORMAT_FLAG, x.getFile().getAbsolutePath());
                JarUtil.copyFiletoDirectory(x.getFile(), Lazy.getFullPath(subPath));
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub

        XClassExecutionExpert constructer = new XClassExecutionExpert();

        constructer.init();
        constructer.execution();

    }

}
