package org.apache.maven.cli;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.cli.event.DefaultEventSpyContext;
import org.apache.maven.cli.internal.BootstrapCoreExtensionManager;
import org.apache.maven.cli.internal.extension.model.CoreExtension;
import org.apache.maven.cli.internal.extension.model.io.xpp3.CoreExtensionsXpp3Reader;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.scope.internal.MojoExecutionScopeModule;
import org.apache.maven.extension.internal.CoreExports;
import org.apache.maven.extension.internal.CoreExtensionEntry;
import org.apache.maven.session.scope.internal.SessionScopeModule;
import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.inject.AbstractModule;

/**
 * @author thunderbolt.lei
 *
 */
public class XMavenCli extends MavenCli {

    private static final Logger LOG = LoggerFactory.getLogger(XMavenCli.class);

    // private Maven maven;

    @Override
    public void initialize(CliRequest cliRequest) throws ExitException {
        // TODO Auto-generated method stub
        super.initialize(cliRequest);
    }

    @Override
    public void cli(CliRequest cliRequest) throws Exception {
        // TODO Auto-generated method stub
        super.cli(cliRequest);
    }

    @Override
    public void logging(CliRequest cliRequest) {
        // TODO Auto-generated method stub
        super.logging(cliRequest);
    }

    @Override
    public void properties(CliRequest cliRequest) {
        // TODO Auto-generated method stub
        super.properties(cliRequest);
    }

    // --------------------------------------------------//

    public PlexusContainer container(CliRequest cliRequest) throws Exception {
        if (cliRequest.classWorld == null) {
            cliRequest.classWorld = new ClassWorld("plexus.core",
                    Thread.currentThread().getContextClassLoader());
        }

        ClassRealm coreRealm = cliRequest.classWorld.getClassRealm("plexus.core");
        if (coreRealm == null) {
            coreRealm = cliRequest.classWorld.getRealms().iterator().next();
        }

        List<File> extClassPath = parseExtClasspath(cliRequest);

        CoreExtensionEntry coreEntry = CoreExtensionEntry.discoverFrom(coreRealm);
        List<CoreExtensionEntry> extensions = loadCoreExtensions(cliRequest, coreRealm,
                coreEntry.getExportedArtifacts());

        ClassRealm containerRealm = setupContainerRealm(cliRequest.classWorld, coreRealm,
                extClassPath, extensions);

        ContainerConfiguration cc = new DefaultContainerConfiguration()
                .setClassWorld(cliRequest.classWorld).setRealm(containerRealm)
                .setClassPathScanning(PlexusConstants.SCANNING_INDEX).setAutoWiring(true)
                .setJSR250Lifecycle(true).setName("maven");
        LOG.info("XMavenCli --->>> \n{}\n", JSON.toJSONString(cc));

        Set<String> exportedArtifacts = new HashSet<>(coreEntry.getExportedArtifacts());
        Set<String> exportedPackages = new HashSet<>(coreEntry.getExportedPackages());
        for (CoreExtensionEntry extension : extensions) {
            exportedArtifacts.addAll(extension.getExportedArtifacts());
            exportedPackages.addAll(extension.getExportedPackages());
        }

        final CoreExports exports = new CoreExports(containerRealm, exportedArtifacts,
                exportedPackages);

        DefaultPlexusContainer container = new DefaultPlexusContainer(cc, new AbstractModule() {
            @Override
            protected void configure() {
                bind(Logger.class).toInstance(LOG);
                bind(CoreExports.class).toInstance(exports);
            }
        });

        // NOTE: To avoid inconsistencies, we'll use the TCCL exclusively for lookups
        container.setLookupRealm(null);
        Thread.currentThread().setContextClassLoader(container.getContainerRealm());

        // container.setLoggerManager(plexusLoggerManager);

        for (CoreExtensionEntry extension : extensions) {
            container.discoverComponents(extension.getClassRealm(),
                    new SessionScopeModule(container), new MojoExecutionScopeModule(container));
        }

        customizeContainer(container);

        container.getLoggerManager().setThresholds(cliRequest.request.getLoggingLevel());

        // eventSpyDispatcher = container.lookup(EventSpyDispatcher.class);

        DefaultEventSpyContext eventSpyContext = new DefaultEventSpyContext();
        Map<String, Object> data = eventSpyContext.getData();
        data.put("plexus", container);
        data.put("workingDirectory", cliRequest.workingDirectory);
        data.put("systemProperties", cliRequest.systemProperties);
        data.put("userProperties", cliRequest.userProperties);
        data.put("versionProperties", CLIReportingUtils.getBuildProperties());
        // eventSpyDispatcher.init(eventSpyContext);

        // refresh logger in case container got customized by spy
        // slf4jLogger = slf4jLoggerFactory.getLogger(this.getClass().getName());

        // maven = container.lookup(Maven.class);

        // executionRequestPopulator =
        // container.lookup(MavenExecutionRequestPopulator.class);

        // modelProcessor = createModelProcessor(container);

        // configurationProcessors = container.lookupMap(ConfigurationProcessor.class);

        // toolchainsBuilder = container.lookup(ToolchainsBuilder.class);

        // dispatcher = (DefaultSecDispatcher) container.lookup(SecDispatcher.class,
        // "maven");

        return container;
    }

    private static final String EXT_CLASS_PATH = "maven.ext.class.path";

    private static final String EXTENSIONS_FILENAME = ".mvn/extensions.xml";

    private List<File> parseExtClasspath(CliRequest cliRequest) {
        String extClassPath = cliRequest.userProperties.getProperty(EXT_CLASS_PATH);
        if (extClassPath == null) {
            extClassPath = cliRequest.systemProperties.getProperty(EXT_CLASS_PATH);
        }

        List<File> jars = new ArrayList<>();

        if (StringUtils.isNotEmpty(extClassPath)) {
            for (String jar : StringUtils.split(extClassPath, File.pathSeparator)) {
                File file = resolveFile(new File(jar), cliRequest.workingDirectory);

                LOG.debug("  Included " + file);

                jars.add(file);
            }
        }

        return jars;
    }

    private List<CoreExtensionEntry> loadCoreExtensions(CliRequest cliRequest,
            ClassRealm containerRealm, Set<String> providedArtifacts) {
        if (cliRequest.multiModuleProjectDirectory == null) {
            return Collections.emptyList();
        }

        File extensionsFile = new File(cliRequest.multiModuleProjectDirectory, EXTENSIONS_FILENAME);
        if (!extensionsFile.isFile()) {
            return Collections.emptyList();
        }

        try {
            List<CoreExtension> extensions = readCoreExtensionsDescriptor(extensionsFile);
            if (extensions.isEmpty()) {
                return Collections.emptyList();
            }

            ContainerConfiguration cc = new DefaultContainerConfiguration() //
                    .setClassWorld(cliRequest.classWorld) //
                    .setRealm(containerRealm) //
                    .setClassPathScanning(PlexusConstants.SCANNING_INDEX) //
                    .setAutoWiring(true) //
                    .setJSR250Lifecycle(true) //
                    .setName("maven");

            DefaultPlexusContainer container = new DefaultPlexusContainer(cc, new AbstractModule() {
                @Override
                protected void configure() {
                    bind(Logger.class).toInstance(LOG);
                }
            });

            try {
                container.setLookupRealm(null);

                // container.setLoggerManager(plexusLoggerManager);

                container.getLoggerManager().setThresholds(cliRequest.request.getLoggingLevel());

                Thread.currentThread().setContextClassLoader(container.getContainerRealm());

                // executionRequestPopulator =
                // container.lookup(MavenExecutionRequestPopulator.class);

                // configurationProcessors = container.lookupMap(ConfigurationProcessor.class);

                // configure(cliRequest);

                MavenExecutionRequest request = DefaultMavenExecutionRequest
                        .copy(cliRequest.request);

                // request = populateRequest(cliRequest, request);

                // request = executionRequestPopulator.populateDefaults(request);

                BootstrapCoreExtensionManager resolver = container
                        .lookup(BootstrapCoreExtensionManager.class);

                return resolver.loadCoreExtensions(request, providedArtifacts, extensions);
            } finally {
                // executionRequestPopulator = null;
                container.dispose();
            }
        } catch (RuntimeException e) {
            // runtime exceptions are most likely bugs in maven, let them bubble up to the
            // user
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to read extensions descriptor " + extensionsFile + ": "
                    + e.getMessage());
        }
        return Collections.emptyList();
    }

    private ClassRealm setupContainerRealm(ClassWorld classWorld, ClassRealm coreRealm,
            List<File> extClassPath, List<CoreExtensionEntry> extensions) throws Exception {
        if (!extClassPath.isEmpty() || !extensions.isEmpty()) {
            ClassRealm extRealm = classWorld.newRealm("maven.ext", null);

            extRealm.setParentRealm(coreRealm);

            LOG.debug("Populating class realm " + extRealm.getId());

            for (File file : extClassPath) {
                LOG.debug("  Included " + file);

                extRealm.addURL(file.toURI().toURL());
            }

            for (CoreExtensionEntry entry : reverse(extensions)) {
                Set<String> exportedPackages = entry.getExportedPackages();
                ClassRealm realm = entry.getClassRealm();
                for (String exportedPackage : exportedPackages) {
                    extRealm.importFrom(realm, exportedPackage);
                }
                if (exportedPackages.isEmpty()) {
                    // sisu uses realm imports to establish component visibility
                    extRealm.importFrom(realm, realm.getId());
                }
            }

            return extRealm;
        }

        return coreRealm;
    }

    private List<CoreExtension> readCoreExtensionsDescriptor(File extensionsFile)
            throws IOException, XmlPullParserException {
        CoreExtensionsXpp3Reader parser = new CoreExtensionsXpp3Reader();
        try (InputStream is = new BufferedInputStream(new FileInputStream(extensionsFile))) {
            return parser.read(is).getExtensions();
        }
    }

    private static <T> List<T> reverse(List<T> list) {
        List<T> copy = new ArrayList<>(list);
        Collections.reverse(copy);
        return copy;
    }

}
