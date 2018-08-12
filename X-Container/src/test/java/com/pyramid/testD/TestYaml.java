package com.pyramid.testD;

import java.io.File;
import java.io.FileInputStream;

import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSON;
import com.pyramid.entities.XYamlEntity;
import com.pyramid.utils.Lazy;

/**
 * @author thunderbolt.lei <br>
 *
 */
public class TestYaml {

    public static void main(String[] args) throws Exception {

        File yamlFile = Lazy.getFullPath("src/main/resources/x-main.yaml");
        FileInputStream fis = new FileInputStream(yamlFile);

        //// <dependency>
        //// <groupId>org.jyaml</groupId>
        //// <artifactId>jyaml</artifactId>
        //// <version>1.3</version>
        //// </dependency>
        // XYamlEntity yamlEntity = (XYamlEntity) Yaml.loadType(yamlFile,
        //// XYamlEntity.class);
        // System.out.println(JSON.toJSONString(yamlEntity, true));

        Yaml yaml = new Yaml();
        // int len = 0;
        // byte[] buf = new byte[1024];
        // while ((len = fis.read(buf)) != -1) {
        // System.out.println(new String(buf, 0, len));
        // }
        // System.out.println("----------");

        XYamlEntity xyaml = yaml.loadAs(fis, XYamlEntity.class);
        System.out.println(JSON.toJSONString(xyaml, true));
        System.out.println(null == xyaml.getPluginRepoList());

    }

}
