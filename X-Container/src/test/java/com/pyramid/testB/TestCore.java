package com.pyramid.testB;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.project.DefaultModelBuildingListener;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingHelper;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.settings.Mirror;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.pyramid.entities.XRepoEntity;
import com.pyramid.factories.XPlexusContainerFactory;
import com.pyramid.manager.LegacyLocalRepositoryManager;
import com.pyramid.templates.executions.AbstractXClassExecution;

/**
 * @author thunderbolt.lei
 *
 */
public class TestCore extends AbstractXClassExecution {

    private static final Logger LOG = LoggerFactory.getLogger(TestCore.class);

    private static final boolean JSON_FORMAT_FLAG = false;

    private static String subPath = "libs/kafka/0.9.0.1", //
            pomPath = subPath + File.separator + "pom.xml"; //
    private static File pomFile = TestModel.getFullPath(pomPath);

    /**
     * To download the dependecy jars according to pom.xml (OK)
     * 
     * @param container
     */
    private void test01(PlexusContainer container) {

        try {
            /**
             * LocalRepository - local
             */
            String local = "local", //
                    repository = "libs/repository"; //
            File localRepoPath = TestModel.getFullPath(repository);
            XRepoEntity xLocalRepo = new XRepoEntity(local,
                    "file://" + localRepoPath.toURI().getPath());
            ArtifactRepository localRepo = mavenWorkFactory.createRepository(xLocalRepo);

            /**
             * RemoteRepository - central
             */
            String central = "central", // central / global
                    remoteRepository = "http://maven.aliyun.com/nexus/content/repositories/central/";
            XRepoEntity xRemoteRepo = new XRepoEntity(central, remoteRepository);
            List<ArtifactRepository> remoteRepoList = new ArrayList<ArtifactRepository>();
            ArtifactRepository remoteRepo = mavenWorkFactory.createRepository(xRemoteRepo);
            remoteRepoList.add(remoteRepo);

            // GOALS
            List<String> goalList = Arrays.asList( //
                    // "package" //
                    "clean", //
                    "test" //
            );

            /**
             * MIRRORS
             */
            Mirror aliMirror = new Mirror();
            aliMirror.setId("alimaven");
            aliMirror.setName("aliyun maven");
            aliMirror.setMirrorOf("central");
            aliMirror.setUrl("http://maven.aliyun.com/nexus/content/repositories/central/");
            aliMirror.setSourceLevel(Mirror.GLOBAL_LEVEL);
            List<Mirror> mirrorList = new ArrayList<Mirror>();
            mirrorList.add(aliMirror);

            // MavenExecutionRequest
            MavenExecutionRequest mavenExecutionRequest = mavenWorkFactory
                    .createMavenExecutionRequest(pomFile, localRepo, remoteRepoList, remoteRepoList,
                            goalList, mirrorList);

            // Maven Project
            MavenProject mavenProject = mavenWorkFactory.createMavenProject(pomFile);
            if (null == mavenProject) {
                throw new Exception("Exception: MavenProject initializes failed.");
            }
            /**
             * Before the maven execution, all values are empty.
             */
            // LOG.info("Test --->>> proj\n{}\n", mavenProject.toString());
            // LOG.info("Test --->>> 1\n{}\n",
            // JSON.toJSONString(mavenProject.getArtifactMap()));
            // LOG.info("Test --->>> 2\n{}\n",
            // JSON.toJSONString(mavenProject.getArtifacts()));
            // LOG.info("Test --->>> 3\n{}\n",
            // JSON.toJSONString(mavenProject.getDependencies()));

            // Maven Execution Result
            MavenExecutionResult mavenExecResult = mavenWorkFactory
                    .doXExecution(mavenExecutionRequest);
            if (null == mavenExecResult) {
                throw new Exception("Maven Project executes failed.");
            }
            /**
             * After the maven project execution, all values are full.
             */
            // mavenProject = mavenExecResult.getProject();
            // LOG.info("Test --->>> proj\n{}\n", mavenProject.toString());
            // LOG.info("Test --->>> 1\n{}\n",
            // JSON.toJSONString(mavenProject.getArtifactMap()));
            // LOG.info("Test --->>> 2\n{}\n",
            // JSON.toJSONString(mavenProject.getArtifacts()));
            // LOG.info("Test --->>> 3\n{}\n",
            // JSON.toJSONString(mavenProject.getDependencies()));

            // All artifacts which are needed as building the maven project.
            List<Artifact> artifactList = Lists
                    .newArrayList(mavenExecResult.getProject().getArtifacts());
            LOG.info("Test --->>> \nartifactList:{}\nsize:{}\n", JSON.toJSONString(artifactList),
                    artifactList.size());
            System.exit(0);

            // ProjectBuildingResult
            ProjectBuildingResult projBuildingResult = mavenWorkFactory
                    .buildProject(mavenExecutionRequest, mavenProject);
            projBuildingResult.getProblems().forEach(x -> {
                LOG.info("--->>> " + JSON.toJSONString(x));
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void test02() throws Exception {
        String subPath = "libs/kafka/0.11.0.3";
        String pomPath = subPath + File.separator + "pom.xml";
        File pomFile = TestModel.getFullPath(pomPath);
        LOG.info("pomFile: " + pomFile.getAbsolutePath());
        // MavenProject mavenProject = XMavenWorkFactory.createMavenProject(pomFile);
        // LOG.info(mavenProject.toString());

        ModelBuilder builder = new DefaultModelBuilderFactory().newInstance();

        DefaultModelBuildingRequest request = new DefaultModelBuildingRequest();
        request.setProcessPlugins(true);
        request.setPomFile(pomFile);

        ModelBuildingResult result = builder.build(request);
        if (!result.getProblems().isEmpty()) {
            result.getProblems().forEach(x -> {
                LOG.info("<<--PROBLEM-->>\n" + JSON.toJSONString(x));
            });
        } else {
            LOG.info("--->>> no problems");
        }
        LOG.info("ModelBuildingResult: " + JSON.toJSONString(result, JSON_FORMAT_FLAG));
    }

    private void test03(PlexusContainer container) throws Exception {
        String subPath = "libs/kafka/0.11.0.3";
        String pomPath = subPath + File.separator + "pom.xml";
        File pomFile = TestModel.getFullPath(pomPath);

        // LocalRepository
        XRepoEntity xLocalRepo = new XRepoEntity("local",
                "file://" + TestModel.getFullPath(subPath));
        ArtifactRepository localRepo = mavenWorkFactory.createRepository(xLocalRepo);

        // RemoteRepository
        XRepoEntity xRemoteRepo = new XRepoEntity("central",
                "http://maven.aliyun.com/nexus/content/repositories/central/");
        List<ArtifactRepository> remoteRepoList = new ArrayList<ArtifactRepository>();
        ArtifactRepository remoteRepo = mavenWorkFactory.createRepository(xRemoteRepo);
        remoteRepoList.add(remoteRepo);

        // GOALS
        List<String> goalList = Arrays.asList( //
                // "package" //
                "clean", //
                "test" //
        );

        ProjectBuildingRequest request = new DefaultProjectBuildingRequest();
        request.setLocalRepository(localRepo);
        request.setRemoteRepositories(remoteRepoList);

        DefaultRepositorySystemSession repoSession = MavenRepositorySystemUtils.newSession();
        repoSession.setLocalRepositoryManager(new LegacyLocalRepositoryManager(pomFile));
        request.setRepositorySession(repoSession);
        request.setResolveDependencies(true);

        // ProjectBuildingResult projBuildingResult = new
        // DefaultProjectBuilder().build(pomFile,
        // request);

        ModelBuildingRequest modelBuildingrequest = new DefaultModelBuildingRequest();

        MavenProject project = new MavenProject();
        project.setFile(pomFile);
        project.setProjectBuildingRequest(request);

        // ProjectBuildingHelper projBuildingHelper = new
        // DefaultProjectBuildingHelper();
        ProjectBuildingHelper projBuildingHelper = container.lookup(ProjectBuildingHelper.class);
        projBuildingHelper.createProjectRealm(project, project.getModel(), request);

        DefaultModelBuildingListener listener = new DefaultModelBuildingListener(project,
                projBuildingHelper, request);

        modelBuildingrequest.setModelBuildingListener(listener);
        modelBuildingrequest.setPomFile(pomFile);
        modelBuildingrequest.setModelSource(new FileModelSource(pomFile));
        modelBuildingrequest.setLocationTracking(true);
        modelBuildingrequest.setProcessPlugins(true);
        modelBuildingrequest.setTwoPhaseBuilding(false);

        ModelBuildingResult result = new DefaultModelBuilderFactory().newInstance()
                .build(modelBuildingrequest);
        result.getProblems().forEach(x -> {
            LOG.info("Test - problems:\n" + JSON.toJSONString(x, JSON_FORMAT_FLAG));
        });
        LOG.info("Test - ProjectBuildingResult: \n" + JSON.toJSONString(result, JSON_FORMAT_FLAG));
    }

    public static void test04() {
        System.out.println("//--- Properties ------------------------------//");
        System.getProperties().forEach((x, y) -> {
            System.out.println("key: " + x + ", value: " + y);
        });
        System.out.println("//--- env ------------------------------//");
        System.getenv().forEach((x, y) -> {
            System.out.println("key: " + x + ", value: " + y);
        });
    }

    public void test05(PlexusContainer container) {
        try {
            // ProjectBuilder
            ProjectBuilder projBuilder = container.lookup(ProjectBuilder.class);

            // Maven Project
            MavenProject mavenProject = mavenWorkFactory.createMavenProject(pomFile);

            // Project Building Request
            ProjectBuildingRequest projBuildingRequest = new DefaultProjectBuildingRequest();
            projBuildingRequest.setProject(mavenProject);

            // Project Building Result
            ProjectBuildingResult projBuildingResult = projBuilder.build(pomFile,
                    projBuildingRequest);

            LOG.info("Test --->>> \n{}\n", projBuildingResult.getProject().getArtifactMap());

        } catch (ComponentLookupException | ProjectBuildingException e) {
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

        // test02();
        //
        // System.exit(0);
        //

        PlexusContainer container = XPlexusContainerFactory.getInstance().getPlexusContainer();
        TestCore test = new TestCore();

        // test05(container);

        test.test01(container);

        // test03(container);

        // test04();

    }

}
