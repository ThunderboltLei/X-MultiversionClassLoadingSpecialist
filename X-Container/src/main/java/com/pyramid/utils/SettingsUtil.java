package com.pyramid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author thunderbolt.lei
 *
 */
public class SettingsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsUtil.class);

    /**
     * This function is for testing...
     * 
     * @param settings
     */
    public static Settings parseSettings(String settingsPath) {
        File globalSettingsFile = new File(settingsPath);
        try (Reader reader = new InputStreamReader(new FileInputStream(globalSettingsFile),
                "UTF-8")) {
            Settings obj = new SettingsXpp3Reader().read(reader);
            obj.setLocalRepository(globalSettingsFile.getParent());
            LOG.info("settings: {}", JSON.toJSONString(obj));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
