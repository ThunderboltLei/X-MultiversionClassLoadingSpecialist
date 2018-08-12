package com.pyramid.testC;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.project.DefaultProjectBuilder;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.ProjectBuilder;
import org.codehaus.plexus.PlexusContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.pyramid.entities.XRepoEntity;
import com.pyramid.factories.XMavenWorkFactory;
import com.pyramid.factories.XPlexusContainerFactory;
import com.pyramid.templates.executions.AbstractXClassExecution;
import com.pyramid.utils.Lazy;

/**
 * @author thunderbolt.lei
 *
 */
public class TestPlexus extends AbstractXClassExecution {

    private static final Logger LOG = LoggerFactory.getLogger(TestPlexus.class);
    private static final boolean JSON_FORMAT_FLAG = false;

    private PlexusContainer container;

    public TestPlexus() {
        try {
            // // ----- METHOD 1 ---------------------------------------------------//
            //
            // String MAVEN_HOME = "/opt/developments/maven-work/apache-maven-3.5.2";
            // System.setProperty(XMavenCli.MULTIMODULE_PROJECT_DIRECTORY, new File(
            // "/opt/developments/eclipse-oxygen3a-j2ee-work/myprojects/thunderboltlei/X-Project/java/X-LoadingClassExpert/libs/kafka/0.11.0.3")
            // .getCanonicalPath());
            // XMavenCli cli = new XMavenCli();
            // XCliRequest request = new XCliRequest(
            // new String[] { "-Dclassworlds.conf=" + MAVEN_HOME + "/bin/m2.conf" //
            // , "-Dmaven.home=" + MAVEN_HOME //
            // , "-Dlibrary.jansi.path=" + MAVEN_HOME + "/lib/jansi-native" },
            // null);
            // cli.initialize(request);
            // cli.cli(request);
            // cli.properties(request);
            // cli.properties(request);
            // container = cli.container(request);
            // LOG.info("Test --->>> \n{}\n", JSON.toJSONString(request));
            // LOG.info("Test --->>> \n{}\n", JSON.toJSONString(container));
            //
            // ProjectBuilder pb1 = container.lookup(ProjectBuilder.class);
            // if (container.hasComponent(ProjectBuilder.class)) {
            // System.out.println("include");
            // } else {
            // System.out.println("exclude");
            // }
            // // System.exit(0);

            // ----- METHOD 2 ---------------------------------------------------//
            container = XPlexusContainerFactory.getInstance().getPlexusContainer();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 
     * @param path
     *            libs/maven
     * @return
     */
    // private List<File> parseExtClasspath() {
    // List<File> jars = new ArrayList<File>();
    // File root = new File(Lazy.BASEDIR + File.separator + "libs/maven");
    // for (File jar : root.listFiles()) {
    // // File file = resolveFile(new File(jar), cliRequest.workingDirectory);
    // jars.add(jar);
    // }
    //
    // return jars;
    // }

    /**
     * 
     * @param classWorld
     * @param coreRealm
     * @param extClassPath
     * @param extensions
     * @return
     * @throws Exception
     */
    // private ClassRealm setupContainerRealm(ClassWorld classWorld, ClassRealm
    // coreRealm,
    // List<File> extClassPath, List<CoreExtensionEntry> extensions) throws
    // Exception {
    // if (!extClassPath.isEmpty() || !extensions.isEmpty()) {
    // ClassRealm extRealm = classWorld.newRealm("X", null);
    //
    // extRealm.setParentRealm(coreRealm);
    //
    // LOG.debug("Populating class realm " + extRealm.getId());
    //
    // for (File file : extClassPath) {
    // LOG.debug(" Included " + file);
    //
    // extRealm.addURL(file.toURI().toURL());
    // }
    //
    // for (CoreExtensionEntry entry : reverse(extensions)) {
    // Set<String> exportedPackages = entry.getExportedPackages();
    // ClassRealm realm = entry.getClassRealm();
    // for (String exportedPackage : exportedPackages) {
    // extRealm.importFrom(realm, exportedPackage);
    // }
    // if (exportedPackages.isEmpty()) {
    // // sisu uses realm imports to establish component visibility
    // extRealm.importFrom(realm, realm.getId());
    // }
    // }
    //
    // return extRealm;
    // }
    //
    // return coreRealm;
    // }

    @SuppressWarnings("unused")
    private static <T> List<T> reverse(List<T> list) {
        List<T> copy = new ArrayList<>(list);
        Collections.reverse(copy);
        return copy;
    }

    // --- Test ----------------------------------------------------------------- //

    private void test01() throws Exception {
        ProjectBuilder projBuilder = (DefaultProjectBuilder) container.lookup(ProjectBuilder.class);
        LOG.info("Test - ProjectBuilder:\n{}\n", JSON.toJSONString(projBuilder, JSON_FORMAT_FLAG));
    }

    private void test02() {
        mavenWorkFactory.testContainerHasClass(new Class[] { //
                ArtifactFactory.class, //
                ArtifactRepositoryFactory.class, //
                ArtifactInstaller.class, //
                ProjectBuilder.class, //
                ModelBuilder.class //
        });
    }

    private void test03() throws Exception {
        // test 01
        LOG.info("baseDir: {}",
                JSON.toJSONString(XMavenWorkFactory.getBasedir(), JSON_FORMAT_FLAG));

        // test 02
        XRepoEntity xLocalRepo = new XRepoEntity("local", "file://" + Lazy.BASEDIR + File.separator
                + "libs/kafka/0.11.0.3" + File.separator + "pom.xml");
        ArtifactRepository localRepository = mavenWorkFactory.createRepository(xLocalRepo);
        DefaultProjectBuildingRequest request = new DefaultProjectBuildingRequest();
        // MavenProject mavenProject = XMavenWorkFactory.buildProject(new File(
        // "/opt/developments/eclipse-oxygen3a-j2ee-work/myprojects/thunderboltlei/X-Project/java/X-LoadingClassExpert/libs/kafka/0.11.0.3/pom.xml")
        // , localRepository);
        // LOG.info("ProjectBuildResult: {}",
        // JSON.toJSONString(mavenProject,JSON_FORMAT_FLAG));
    }

    private void test04() {

    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub

        TestPlexus tp = new TestPlexus();

        // tp.test01();

        tp.test02();

        // tp.test03();

    }

}
