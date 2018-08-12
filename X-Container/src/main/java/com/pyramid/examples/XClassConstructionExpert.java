package com.pyramid.examples;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.pyramid.annotations.XClassAnnotation;
import com.pyramid.annotations.XJarAnnotation;
import com.pyramid.entities.XClassAnnotationEntity;
import com.pyramid.loaders.XClassLoader;
import com.pyramid.templates.constructions.AbstractXClassConstruction;

/**
 * @author thunderbolt.lei
 *
 */
public class XClassConstructionExpert extends AbstractXClassConstruction<XClassConstructionExpert> {

    private static final Logger LOG = LoggerFactory.getLogger(XClassConstructionExpert.class);

    @XJarAnnotation(jarPath = "libs/kafka/0.11.0.3", jarName = "kafka", version = "0.11.0.3")
    @XClassAnnotation(t = XClassConstructionExpert.class)
    static XClassAnnotationEntity xEntity11;

    @XJarAnnotation(jarPath = "libs/kafka/0.9.0.1", jarName = "kafka", version = "0.9.0.1")
    @XClassAnnotation(t = XClassConstructionExpert.class)
    static XClassAnnotationEntity xEntity9;

    // PS: Do not use constructor
    // public XClassExpert() {
    // }

    @Override
    public void init(Class<XClassConstructionExpert> clazz) {
        // TODO Auto-generated method stub
        super.init(clazz);
    }

    @Override
    public void construct() {

        xClassPool.executeTask(new FutureTask<Object>(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                // TODO Auto-generated method stub
                Long threadID = Thread.currentThread().getId();
                System.out.println("--->>> " + threadID);
                return threadID;
            }

        }));

        super.init(XClassConstructionExpert.class);

        LOG.info("xEntity11 == null = " + (null == xEntity11));
        // LOG.info("xEntity9 == null = " + (null == xEntity9));
        LOG.info("--->>>>> {}\n{}", xEntity11.getKey(),
                JSON.toJSONString(xEntity11.getxClassLoader()));
        XClassLoader xLoader = xEntity11.getxClassLoader();
        LOG.info("--->>> {}", xLoader.getURLs().length);

        try {
            Class c11_KafkaProducer = xLoader
                    .loadClass("org.apache.kafka.clients.producer.KafkaProducer");
            Class c11_StringSerializer = xLoader
                    .loadClass("org.apache.kafka.common.serialization.StringSerializer");
            Class c11_DefaultPartitioner = xLoader
                    .loadClass("org.apache.kafka.clients.producer.internals.DefaultPartitioner");
            Class c11_ProducerRecord = xLoader
                    .loadClass("org.apache.kafka.clients.producer.ProducerRecord");

            Properties props = new Properties();
            props.put("bootstrap.servers", "mock194.bd.com:9092");
            props.put("key.serializer", c11_StringSerializer);
            props.put("value.serializer", c11_StringSerializer);
            props.put("acks", "all");
            props.put("retries", 1);
            // props.put("request.required.acks", "-1");
            props.put("partitioner.class", c11_DefaultPartitioner);

            Object producer = ConstructorUtils.invokeConstructor(c11_KafkaProducer, props);
            LOG.info("--->>> {}", JSON.toJSONString(producer.getClass()));

            Object producerRecord = ConstructorUtils.invokeConstructor(c11_ProducerRecord,
                    new Object[] { "t01", "{\"hello\": \"world\"}" });

            for (int i = 0; i < 10; i++) {
                Object send = MethodUtils.invokeMethod(producer, "send", producerRecord);
                LOG.info("--->>> {}", JSON.toJSONString(send));

                Thread.sleep(2000);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        XClassConstructionExpert xce = new XClassConstructionExpert();

        xce.construct();

    }

}
