package com.pyramid.testB;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.impl.MetadataGeneratorFactory;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author thunderbolt.lei
 *
 */
public class TestResolver {

    private static final Logger LOG = LoggerFactory.getLogger(TestResolver.class);

    /**
     * Test RepositorySystem
     */
    public static RepositorySystem test01() {
        ServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        RepositorySystem repoSys = null;
        {
            repoSys = locator.getService(RepositorySystem.class);
        }
        LOG.info("--->>> " + JSON.toJSONString(repoSys));

        return repoSys;
    }

    public static void test02() {
        ServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        MetadataGeneratorFactory genFactory = locator.getService(MetadataGeneratorFactory.class);
        LOG.info("--->>> " + JSON.toJSONString(genFactory, true));
    }

    public static void test03() {
        RepositorySystemSession repoSession = MavenRepositorySystemUtils.newSession();
        LOG.info("\n{}", repoSession.getLocalRepository());
    }

    public static void main(String[] args) {

        test01();

        test02();

        test03();
    }

}
