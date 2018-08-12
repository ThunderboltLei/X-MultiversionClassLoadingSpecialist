package com.pyramid.factories;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.extension.internal.CoreExports;
import org.apache.maven.extension.internal.CoreExtensionEntry;
import org.apache.maven.project.ExtensionDescriptor;
import org.apache.maven.project.ExtensionDescriptorBuilder;
import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.pyramid.loaders.XClassLoader;
import com.pyramid.utils.JarUtil;

/**
 * @author thunderbolt.lei <br>
 * 
 *         XPlexusContainer is the basic plexus container, which is used to deal
 *         the plexus component annotation, then structure the instance.<br>
 *
 */
@Singleton
public class XPlexusContainerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(XPlexusContainerFactory.class);

    private static XPlexusContainerFactory XPlexusContainerFactory;

    private PlexusContainer plexusContainer;

    private XPlexusContainerFactory() {
        // No allow to build with constructure.
        try {
            init();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        /**
         * all jars in libs/maven
         */
        // List<File> extClassPath = parseExtClasspath();
        List<File> jarList = JarUtil.getAllJars("libs/maven");

        // url list
        List<URL> urlList = new ArrayList<URL>();
        jarList.forEach(x -> {
            URL url = null;
            try {
                url = x.toURI().toURL();
                urlList.add(url);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // LOG.info("Test --->>> \n{}\n", url.getFile());
            }
        });
        // LOG.info("Test --->>> \n{}\n", urlList.size());

        XClassLoader xCL = new XClassLoader(urlList.toArray(new URL[urlList.size()]),
                Thread.currentThread().getContextClassLoader());
        // LOG.info("--->>> XClassLoader:\n{}\n", JSON.toJSONString(xCL));
        String realmID = "maven";
        ClassWorld cw = new ClassWorld(realmID, xCL);
        // LOG.info("--->>> ClassWorld:\n{}\n", JSON.toJSONString(cw));
        ClassRealm coreRealm = cw.getClassRealm(realmID);
        // LOG.info("--->>> ClassRealm:\n{}\n", JSON.toJSONString(coreRealm));

        CoreExtensionEntry coreEntry = CoreExtensionEntry.discoverFrom(coreRealm);
        // List<CoreExtensionEntry> extensions = new ArrayList();

        // ClassRealm containerRealm = setupContainerRealm(cw, coreRealm, extClassPath,
        // extensions);

        ExtensionDescriptorBuilder builder = new ExtensionDescriptorBuilder();
        Enumeration<URL> urls = coreRealm.getResources(builder.getExtensionDescriptorLocation());
        Set<String> artifacts = new LinkedHashSet<>();
        Set<String> packages = new LinkedHashSet<>();
        while (urls.hasMoreElements()) {
            try (InputStream is = urls.nextElement().openStream()) {
                ExtensionDescriptor descriptor = builder.build(is);
                artifacts.addAll(descriptor.getExportedArtifacts());
                packages.addAll(descriptor.getExportedPackages());
            }
        }
        // LOG.info("Test --->>> \n{}\n{}\n", JSON.toJSONString(artifacts),
        // JSON.toJSONString(packages));

        // String configurationPath =
        // "/opt/developments/eclipse-oxygen3a-j2ee-work/myprojects/thunderboltlei/X-Project/java/X-LoadingClassExpert/src/test/resources/META-INF/plexus/components.xml";
        ContainerConfiguration cc = new DefaultContainerConfiguration() //
                // .setContainerConfiguration(configurationPath) //
                .setClassWorld(cw) //
                .setRealm(coreRealm) //
                .setComponentVisibility(PlexusConstants.GLOBAL_VISIBILITY) //
                .setClassPathScanning(PlexusConstants.SCANNING_ON) //
                .setAutoWiring(true) //
                .setJSR250Lifecycle(true) //
                .setName("X");
        // LOG.info("Test --->>> \n{}\n", JSON.toJSONString(cc));

        final CoreExports exports = new CoreExports(coreRealm, artifacts, packages);
        // LOG.info("Test --->>> \n{}\n", JSON.toJSONString(exports));

        plexusContainer = new DefaultPlexusContainer(cc, new AbstractModule() {
            @Override
            protected void configure() {
                bind(Logger.class).toInstance(LOG);
                bind(CoreExports.class).toInstance(exports);
            }
        });
        plexusContainer.setLookupRealm(null);
        plexusContainer.discoverComponents(coreRealm);
        // return plexusContainer;
    }

    public static XPlexusContainerFactory getInstance() {
        if (null == XPlexusContainerFactory) {
            return new XPlexusContainerFactory();
        } else {
            return XPlexusContainerFactory;
        }
    }

    public PlexusContainer getPlexusContainer() {
        return plexusContainer;
    }

    public void setPlexusContainer(PlexusContainer plexusContainer) {
        this.plexusContainer = plexusContainer;
    }

}
