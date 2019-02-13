package com.pyramid.testA;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * @author thunderbolt.lei <br>
 *
 * @description
 *
 */
public class Test01 {

    /**
     * @Description: <br>
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        ArrayList arrayList = Records.newRecord(java.util.ArrayList.class);
        arrayList.add("hello");
        arrayList.add("world");
        System.out.println("--->>> " + JSONObject.toJSONString(arrayList));

        System.out.println(new Date(System.nanoTime() / 1000000L));

        String obj0 = "hello", //
                obj1 = "world", //
                obj2 = "hadoop", //
                obj3 = "spark"; //

        List list = new ArrayList();
        list.add(obj0);
        list.add(obj1);
        System.out.println("list: " + JSONObject.toJSONString(list));

        list.set(list.indexOf(obj0), obj2);
        list.set(list.indexOf(obj1), obj3);
        System.out.println("list: " + JSONObject.toJSONString(list));
    }

}
