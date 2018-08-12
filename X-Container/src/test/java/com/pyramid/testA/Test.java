package com.pyramid.testA;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author thunderbolt.lei
 *
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Map<String, String> map1 = Maps.newHashMap(), //
                map2 = Maps.newHashMap(); //

        map1.put("test01", "value01");
        map1.put("test02", "value02");

        map2.put("test01", "value01");
        map2.put("test02", "value02");

        System.out.println(map1.equals(map2));

    }

}
