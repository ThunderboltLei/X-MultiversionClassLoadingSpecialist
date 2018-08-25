package com.pyramid.testA;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.pyramid.http.client.DistributionHttpClient;

/**
 * @author thunderbolt.lei <br>
 *
 *         Testing
 */
public class Test {

    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    // client
    // ContentResponse: {
    // "content": "eyJzdGF0ZSI6MjAwLCJtZXNzYWdlIjoic3VjY2VzcyJ9",
    // "contentAsString": "{\"state\":200,\"message\":\"success\"}",
    // "encoding": "UTF-8",
    // "headers": {
    // "fieldNames": ["Content-Length", "Server", "Date", "Content-Type"],
    // "fieldNamesCollection": ["Content-Length", "Server", "Date", "Content-Type"]
    // },
    // "mediaType": "text/html",
    // "reason": "OK",
    // "request": {
    // "agent": "Jetty/9.4.2.v20170220",
    // "attributes": {},
    // "cookies": [],
    // "followRedirects": false,
    // "headers": {
    // "fieldNames": ["User-Agent", "Host", "Accept-Encoding"],
    // "fieldNamesCollection": ["User-Agent", "Host", "Accept-Encoding"]
    // },
    // "host": "172.17.170.194",
    // "idleTimeout": 0,
    // "method": "POST",
    // "params": {
    // "empty": false,
    // "names": ["slave"],
    // "size": 1
    // },
    // "path": "/rest/v2/heartbeat",
    // "port": 18633,
    // "query": "slave=172.17.170.194",
    // "scheme": "http",
    // "timeout": 0,
    // "uRI": "http://172.17.170.194:18633/rest/v2/heartbeat?slave=172.17.170.194",
    // "version": "HTTP_1_1"
    // },
    // "status": 200,
    // "version": "HTTP_1_1"
    // }
    private void test02() {
        String url = "http://172.17.170.194:18633/rest/v2/heartbeat?slave=172.17.170.194";
        try {
            Map<String, Object> retMap = DistributionHttpClient.requestGETWithNoSSL(url, null);
            LOG.info("--->>> test02 - retMap: {}", JSON.toJSONString(retMap));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * get with no ssl
     * 
     * @TestResult
     */
    private void testRequestGETWithNoSSL() {
        String url = "http://172.17.170.194:18633/rest/v2/heartbeat?slave=172.17.170.194";
        try {
            Map<String, Object> retMap = DistributionHttpClient.requestGETWithNoSSL(url, null);
            LOG.info("--->>> testRequestGETWithNoSSL - retMap: {}", JSON.toJSONString(retMap));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * post with no ssl
     * 
     * @TestResult
     */
    private void testRequestPOSTWithNoSSL() {
        String url = "http://172.17.170.194:18681/rest/v1/pipeline/t02e3334bd5-c201-456b-9f6b-efa7a7af8d8d/start";
        List list = new ArrayList();
        list.add(new BasicNameValuePair("ret", "0"));
        try {
            Map<String, Object> retMap = DistributionHttpClient.requestPOSTWithNoSSL(url, list);
            LOG.info("--->>> testRequestPOSTWithNoSSL - retMap: {}", JSON.toJSONString(retMap));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * put with no ssl
     * 
     * @TestResult
     */
    private void testRequestPUTWithNoSSL() {
        String url = "http://172.17.170.194:18633/rest/v2/heartbeat?slave=172.17.170.194";
        try {
            Map<String, Object> retMap = DistributionHttpClient.requestPUTWithNoSSL(url, null);
            LOG.info("--->>> testRequestPUTWithNoSSL - retMap: {}", JSON.toJSONString(retMap));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * delete with no ssl
     * 
     * @TestResult
     */
    private void testRequestDELETEWithNoSSL() {
        String url = "http://172.17.170.194:18633/rest/v2/heartbeat?slave=172.17.170.194";
        try {
            Map<String, Object> retMap = DistributionHttpClient.requestDELETEWithNoSSL(url, null);
            LOG.info("--->>> testRequestDELETEWithNoSSL - retMap: {}", JSON.toJSONString(retMap));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * get with ssl
     * 
     * @TestResult
     */
    private void testRequestGetWithSSL() {
        String url = "http://172.17.170.194:18681/rest/v1/pipeline/t0189f307f5-ca82-413a-8e64-a9f87cbace87/history?rev=0";
        Map<String, Object> retMap = DistributionHttpClient.requestGETWithSSL(url, "172.17.170.194",
                18681, "admin", "admin", null);
        LOG.info("--->>> testRequestGetWithSSL - retVal: {}", retMap);
    }

    /**
     * post with ssl
     * 
     * @TestResult ok
     */
    private void testRequestPostWithSSL() {
        String url = "http://172.17.170.194:18681/rest/v1/pipeline/t02e3334bd5-c201-456b-9f6b-efa7a7af8d8d/stop?rev=0";
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("rev", "0"));
        list.add(new BasicNameValuePair("runtimeParameters", "{}"));
        Map<String, Object> retMap = DistributionHttpClient.requestPOSTWithSSL(url,
                "172.17.170.194", 18681, "admin", "admin", null);
        LOG.info("--->>> testRequestPostWithSSL - retVal: {}", retMap);
    }

    /**
     * put with ssl
     * 
     * @TestResult
     * 
     */
    private void testRequestPutWithSSL() {
        String url = "http://172.17.170.194:18681/rest/v1/pipeline/t15?description=t15&autoGeneratePipelineId=true&draft=true";
        Map<String, Object> retMap = DistributionHttpClient.requestPUTWithSSL(url, "172.17.170.194",
                18681, "admin", "admin", null);
        LOG.info("--->>> testRequestPutWithSSL - retVal: {}", retMap);
    }

    /**
     * delete with ssl
     * 
     * @TestResult
     * 
     */
    private void testRequestDeleteWithSSL() {
        String url = "http://172.17.170.194:18681/rest/v1/pipelines";
        Map<String, Object> retMap = DistributionHttpClient.requestDELETEWithSSL(url,
                "172.17.170.194", 18681, "admin", "admin", null);
        LOG.info("--->>> testRequestDeleteWithSSL - retVal: {}", retMap);
    }

    private void testNodeState() {
        // LOG.info("--->>> {}", NodeStateEnum.NORMAL.name());
    }

    private void test03() {
        long a = System.currentTimeMillis();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long b = System.currentTimeMillis();
        LOG.info("a = {}, b = {}, b - a = {}", a, b, (b - a) >= 6000L);

        try {
            InetAddress address = InetAddress.getLocalHost();
            LOG.info("--->>> address: {}", JSON.toJSONString(address));
            String hostAddress = address.getHostAddress();
            LOG.info("--->>> hostAddress:{}", hostAddress);
            String canonicalHostName = address.getCanonicalHostName();
            LOG.info("--->>> canonicalHostName:{}", canonicalHostName);
            // LOG.info("--->>> {}", IPUtil.getServerIp());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void testTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                LOG.info("--->>> i: ");
            }

        }, 0, 1000L);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Test test = new Test();

        // new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        // // TODO Auto-generated method stub
        // test.testTimer();
        // }
        //
        // }).start();

        // test.test03();

        // test.test01();

        // test.test02();

        // test.testRequestGETWithNoSSL();

        // test.testRequestGETWithNoSSL();

        // test.testRequestPOSTWithNoSSL();

        // test.testRequestDELETEWithNoSSL();

        // test.testRequestGetWithSSL();

        // test.testRequestPostWithSSL();

        test.testRequestPutWithSSL();

        // test.testRequestDeleteWithSSL();

        // test.testNodeState();

    }

}
