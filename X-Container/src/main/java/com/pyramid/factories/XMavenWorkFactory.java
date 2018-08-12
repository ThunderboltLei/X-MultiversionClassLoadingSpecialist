package com.pyramid.factories;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.maven.Maven;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.artifact.repository.layout.FlatRepositoryLayout;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.project.DefaultModelBuildingListener;
import org.apache.maven.project.DefaultProjectBuildingHelper;
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
import org.eclipse.aether.internal.impl.SimpleLocalRepositoryManagerFactory;
import org.eclipse.aether.repository.LocalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.pyramid.entities.XRepoEntity;
import com.pyramid.entities.XYamlEntity;
import com.pyramid.manager.LegacyLocalRepositoryManager;
import com.pyramid.utils.Lazy;

/**
 * @author thunderbolt.lei
 *
 */
@Singleton
public class XMavenWorkFactory {

    private static final Logger LOG = LoggerFactory.getLogger(XMavenWorkFactory.class);
    private static final boolean JSON_FORMAT_FLAG = false;

    private static XMavenWorkFactory XMavenWorkFactory;

    private XPlexusContainerFactory plexusContainerFactory;
    private PlexusContainer plexusContainer;
    private ArtifactFactory artifactFactory;
    private ArtifactRepositoryFactory artifactRepositoryFactory;
    private ArtifactInstaller artifactInstaller;
    private ArtifactRepositoryLayout repoLayout;
    private ProjectBuilder projectBuilder;
    private ModelBuilder modelBuilder;

    public static String getBasedir() {
        return Lazy.BASEDIR;
    }

    // PS: Not allow to instantiate this class.
    private XMavenWorkFactory() {
        init();
    }

    protected void init() {
        plexusContainerFactory = XPlexusContainerFactory.getInstance();
        plexusContainer = plexusContainerFactory.getPlexusContainer();
        if (null == plexusContainer) {
            try {
                throw new Exception("Exception: PlexusContainer is null, needs to be initialized.");
            } catch (Exception e) {
                // To do nothing
            }
        }

        try {
            artifactFactory = plexusContainer.lookup(ArtifactFactory.class);
            artifactRepositoryFactory = plexusContainer.lookup(ArtifactRepositoryFactory.class);
            artifactInstaller = plexusContainer.lookup(ArtifactInstaller.class);
            projectBuilder = plexusContainer.lookup(ProjectBuilder.class);
            modelBuilder = plexusContainer.lookup(ModelBuilder.class);
        } catch (ComponentLookupException e) {
            try {
                throw new Exception("Exception: PlexusContainer doesn't look up any class. "
                        + JSON.toJSONString(e));
            } catch (Exception e1) {
                // To do nothing
            }
        }

    }

    public static XMavenWorkFactory getInstance() {
        if (null == XMavenWorkFactory) {
            return new XMavenWorkFactory();
        } else {
            return XMavenWorkFactory;
        }
    }

    /**
     * 
     * @param executionRequest
     *            the maven request to execute.
     * @param mavenProject
     *            the maven project which is to be built.
     * @return
     */
    public ProjectBuildingResult buildProject(MavenExecutionRequest executionRequest,
            MavenProject mavenProject) {
        try {
            ProjectBuildingRequest projBuildingRequest = new DefaultProjectBuildingRequest();
            projBuildingRequest.setResolveDependencies(true);
            projBuildingRequest.setLocalRepository(executionRequest.getLocalRepository());
            projBuildingRequest.setRemoteRepositories(executionRequest.getRemoteRepositories());
            if (null != executionRequest.getPluginArtifactRepositories()) {
                projBuildingRequest.setPluginArtifactRepositories(
                        executionRequest.getPluginArtifactRepositories());
            }
            projBuildingRequest.setProject(mavenProject);
            projBuildingRequest.setResolveDependencies(true);
            // return projectBuilder.build(executionRequest.getPom(), projBuildingRequest);

            DefaultRepositorySystemSession repoSession = MavenRepositorySystemUtils.newSession();
            repoSession.setLocalRepositoryManager(
                    new LegacyLocalRepositoryManager(executionRequest.getPom()));
            projBuildingRequest.setRepositorySession(repoSession);

            ModelBuildingRequest modelBuildingrequest = new DefaultModelBuildingRequest();

            MavenProject project = new MavenProject();
            project.setFile(executionRequest.getPom());

            ProjectBuildingHelper projBuildingHelper = new DefaultProjectBuildingHelper();
            DefaultModelBuildingListener listener = new DefaultModelBuildingListener(project,
                    projBuildingHelper, projBuildingRequest);

            modelBuildingrequest.setModelBuildingListener(listener);
            modelBuildingrequest.setPomFile(executionRequest.getPom());
            // modelBuildingrequest.setModelSource(new
            // FileModelSource(executionRequest.getPom()));
            modelBuildingrequest.setLocationTracking(true);
            modelBuildingrequest.setProcessPlugins(false);
            // modelBuildingrequest.setTwoPhaseBuilding(false);
            // LOG.info("modelBuildingrequest: \n" +
            // JSON.toJSONString(modelBuildingrequest));

            ModelBuildingResult result = modelBuilder.build(modelBuildingrequest);
            result.getProblems().forEach(x -> {
                LOG.info("<<--PROBLEM-->>\n" + JSON.toJSONString(x));
            });
            LOG.info("ModelBuildingResult: " + JSON.toJSONString(result, JSON_FORMAT_FLAG));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param executionRequest
     * @return
     */
    public MavenExecutionResult doXExecution(MavenExecutionRequest mavenExecutionRequest) {
        try {
            Maven maven = plexusContainer.lookup(Maven.class);
            return maven.execute(mavenExecutionRequest);
            // LOG.info("Test --->>> \n{}\n",
            // JSON.toJSONString(mavenExecResult.getProject().getArtifactMap()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param pomFile
     * @param yamlEntity
     * @return
     * @throws Exception
     */
    public MavenExecutionRequest createMavenExecutionRequest(File pomFile, XYamlEntity yamlEntity)
            throws Exception {
        // localRepo
        ArtifactRepository localRepo = createRepository(yamlEntity.getLocalRepo());
        // RemoteRepoList
        List<ArtifactRepository> remoteRepoList = Lists.newArrayList();
        yamlEntity.getRemoteRepoList().forEach(x -> {
            try {
                remoteRepoList.add(createRepository(x));
            } catch (InvalidRepositoryException e) {
                e.printStackTrace();
            }
        });
        // PluginRepoList
        final List<ArtifactRepository> pluginRepoList = Lists.newArrayList();
        if (null == yamlEntity.getPluginRepoList() || yamlEntity.getPluginRepoList().isEmpty()) {
            pluginRepoList.addAll(remoteRepoList);
        } else {
            yamlEntity.getPluginRepoList().forEach(x -> {
                try {
                    pluginRepoList.add(createRepository(x));
                } catch (InvalidRepositoryException e) {
                    e.printStackTrace();
                }
            });
        }
        // GoalList - yamlEntity.getGoalList()
        // MirrorList - yamlEntity.getMirrorList()
        return createMavenExecutionRequest(pomFile, localRepo, remoteRepoList, remoteRepoList,
                yamlEntity.getGoalList(), yamlEntity.getMirrorList());
    }

    /**
     * To create an execution request for maven project.
     * 
     * @param pomFile
     *            file of pom.xml
     * @param localRepository
     *            local repository
     * @param remoteRepoList
     *            list of remote repositories
     * @param pluginRepoList
     *            list of plugin repositories
     * @param goalList
     *            list of goals for execution
     * @return
     * @throws Exception
     */
    public MavenExecutionRequest createMavenExecutionRequest(File pomFile,
            ArtifactRepository localRepository, List<ArtifactRepository> remoteRepoList,
            List<ArtifactRepository> pluginRepoList, List<String> goalList, List<Mirror> mirrorList)
            throws Exception {
        MavenExecutionRequest request = new DefaultMavenExecutionRequest() //
                .setPom(pomFile) // file of pom.xml
                .setProjectPresent(true) //
                .setShowErrors(true) // showing errors
                .setLoggingLevel(1) //
                .setGlobalChecksumPolicy(ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE)
                .setPluginGroups(Arrays.asList("org.apache.maven.plugins")); // plugins
        if (null != localRepository) {
            request.setLocalRepository(localRepository); //
        }
        if (null != remoteRepoList) {
            request.setRemoteRepositories(remoteRepoList); //
        }
        if (null != pluginRepoList) {
            request.setPluginArtifactRepositories(pluginRepoList); //
        }
        if (null != goalList) {
            request.setGoals(goalList);
        }
        if (null != mirrorList) {
            request.setMirrors(mirrorList);
        }
        // LOG.info("MavenExecutionRequest:\n" + JSON.toJSONString(request) + "\n");
        return request;
    }

    /**
     * To create repository.
     * 
     * @param repoID
     *            local, remote, global
     * @param localRepository
     *            local -> file://+path <br>
     *            remote -> url <br>
     *            global -> url <br>
     * @return
     * @throws InvalidRepositoryException
     */
    public ArtifactRepository createRepository(XRepoEntity xRepo)
            throws InvalidRepositoryException {
        MavenArtifactRepository artifactRepository = new MavenArtifactRepository( //
                xRepo.getMirrorOf(), // repoID
                xRepo.getUrl(), // repository
                new DefaultRepositoryLayout(), // layout
                new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_DAILY,
                        ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN), // snapshot
                new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_DAILY,
                        ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN)); // release
        // LOG.info("ArtifactRepository: [" + repoID + "]\n" +
        // artifactRepository.toString());
        return artifactRepository;
    }

    /**
     * 
     * @param executionRequest
     * @param executionProperties
     * @return
     */
    public ProjectBuildingRequest createProjectBuildingRequest(
            MavenExecutionRequest executionRequest, Properties executionProperties) {
        ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest()
                .setLocalRepository(executionRequest.getLocalRepository())
                .setRemoteRepositories(executionRequest.getRemoteRepositories())
                .setPluginArtifactRepositories(executionRequest.getPluginArtifactRepositories());
        if (null != executionProperties) {
            buildingRequest.setSystemProperties(executionProperties);
        }
        return buildingRequest;
    }

    /**
     * 
     * @param pomFile
     * @param executionRequest
     * @param buildingRequest
     * @param includeModules
     * @return
     * @throws Exception
     */
    public MavenSession createMavenSession(File pomFile, MavenExecutionRequest executionRequest,
            ProjectBuildingRequest buildingRequest, boolean includeModules) throws Exception {

        List<MavenProject> projects = new ArrayList<>();

        if (pomFile != null) {
            MavenProject project = projectBuilder.build(pomFile, buildingRequest).getProject();

            projects.add(project);
            if (includeModules) {
                for (String module : project.getModules()) {
                    File modulePom = new File(pomFile.getParentFile(), module);
                    if (modulePom.isDirectory()) {
                        modulePom = new File(modulePom, "pom.xml");
                    }
                    projects.add(projectBuilder.build(modulePom, buildingRequest).getProject());
                }
            }
        } else {
            MavenProject project = createMavenProject(pomFile);
            project.setRemoteArtifactRepositories(executionRequest.getRemoteRepositories());
            project.setPluginArtifactRepositories(executionRequest.getPluginArtifactRepositories());
            projects.add(project);
        }

        initRepoSession(buildingRequest);

        MavenSession session = new MavenSession(plexusContainer,
                buildingRequest.getRepositorySession(), executionRequest,
                new DefaultMavenExecutionResult());
        session.setProjects(projects);
        session.setAllProjects(session.getProjects());

        return session;
    }

    /**
     * 
     * @param request
     * @throws Exception
     */
    protected void initRepoSession(ProjectBuildingRequest request) throws Exception {
        File localRepoDir = new File(request.getLocalRepository().getBasedir());
        LocalRepository localRepo = new LocalRepository(localRepoDir);
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        session.setLocalRepositoryManager(
                new SimpleLocalRepositoryManagerFactory().newInstance(session, localRepo));
        request.setRepositorySession(session);
    }

    /**
     * To create a temporary maven project.
     * 
     * @param pomFile
     *            the pom.xml file of maven project
     * @return
     */
    public MavenProject createMavenProject(File pomFile) {
        ModelBuilder builder = new DefaultModelBuilderFactory().newInstance();
        DefaultModelBuildingRequest modelBuildingRequest = new DefaultModelBuildingRequest();
        modelBuildingRequest.setProcessPlugins(true);
        modelBuildingRequest.setPomFile(pomFile);
        ModelBuildingResult result = null;
        try {
            result = builder.build(modelBuildingRequest);
            Model model = result.getRawModel();
            return new MavenProject(model);
        } catch (ModelBuildingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The result of project building.
     * 
     * @param pomFile
     *            pom.xml
     * @param request
     *            ProjectBuildingRequest
     * @return
     */
    public ProjectBuildingResult buildResult(File pomFile, ProjectBuildingRequest request) {
        try {
            return projectBuilder.build(pomFile, request);
        } catch (ProjectBuildingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * To move the jars from local directory to specified directory
     * 
     * @param artifacts
     */
    public void installJars(List<Artifact> artifacts) {

        /**
         * 将JAR包下载到指定路径下
         */
        // // Examples:
        // String artifactBasedir = new File(getBasedir(),
        // "src/test/resources/artifact-install")
        // .getAbsolutePath();
        // Artifact artifact = createArtifact("artifact", "1.0");
        // File source = new File(artifactBasedir, "artifact-1.0.jar");
        // artifactInstaller.install(source, artifact, localRepository());

        String artifactBaseDir = null;
        artifacts.forEach(x -> {
            LOG.info("Artifact: {}", JSON.toJSONString(x, JSON_FORMAT_FLAG));
            File source = new File(artifactBaseDir, x.getFile().getName() + ".jar");
            artifactFactory.createBuildArtifact(x.getGroupId(), x.getArtifactId(), x.getVersion(),
                    x.getType());
            try {
                artifactInstaller.install(source, x, localRepository());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

    }

    /**
     * 
     * @return
     * @throws Exception
     */
    private ArtifactRepository localRepository() throws Exception {
        String path = "target/test-repositories/local-repository";

        File f = new File(getBasedir(), path);

        // ArtifactRepositoryLayout repoLayout = (ArtifactRepositoryLayout) lookup(
        // ArtifactRepositoryLayout.ROLE, "default");
        repoLayout = new FlatRepositoryLayout();

        return artifactRepositoryFactory.createArtifactRepository("local", "file://" + f.getPath(),
                repoLayout, null, null);
    }

    /**
     * For test: validate the plexus container whether has a class.
     * 
     * @param args
     * @throws Exception
     */
    public void testContainerHasClass(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (plexusContainer.hasComponent(clazz)) {
                LOG.info("The container has [" + clazz.getName() + "].");
            } else {
                LOG.info("The container doesn't have [" + clazz.getName() + "].");
            }
        }
    }

}