package com.pyramid.testB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.pyramid.utils.Lazy;

/**
 * @author thunderbolt.lei
 *
 */
public class TestModel {

    private static final Logger LOG = LoggerFactory.getLogger(TestModel.class);

    public static File getFullPath(String subPath) {
        return new File(Lazy.BASEDIR + File.separator + subPath);
    }

    public static void test01() {
        ModelBuilder builder = new DefaultModelBuilderFactory().newInstance();
        File pomFile = getFullPath("libs/kafka/0.11.0.3/pom.xml");
        DefaultModelBuildingRequest request = new DefaultModelBuildingRequest();
        request.setProcessPlugins(true);
        request.setPomFile(pomFile);
        try {
            ModelBuildingResult result = builder.build(request);
            LOG.info("--->>> {}", JSON.toJSONString(result.getRawModel(), true));
            
        } catch (ModelBuildingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<String>();
        list.add("hello");
        list.add("world");
        if (null != list || list.isEmpty() == false) {
            System.out.println("hello world");
        }

        test01();

    }

}
