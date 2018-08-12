package com.pyramid.testB;

import java.io.File;

import org.apache.maven.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.pyramid.utils.Lazy;
import com.pyramid.utils.SettingsUtil;

/**
 * @author thunderbolt.lei
 *
 */
public class TestSettings {

    private static final Logger LOG = LoggerFactory.getLogger(TestSettings.class);

    private static File getSettingsFile(String subSettingsFile) {
        return new File(Lazy.BASEDIR + File.separator + subSettingsFile);
    }

    private static void test01() {
        String settingsPath = getSettingsFile("libs/settings.xml").getAbsolutePath();
        Settings settings = SettingsUtil.parseSettings(settingsPath);
        LOG.info(JSON.toJSONString(settings.getLocalRepository(), true));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        test01();

    }

}
