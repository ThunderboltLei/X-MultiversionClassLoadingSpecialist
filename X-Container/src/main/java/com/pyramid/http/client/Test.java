package com.pyramid.http.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author thunderbolt.lei
 *
 */
public class Test {

    private String username = "admin", //
            password = "admin", //
            host = "172.17.170.194"; //
    private int port = 18681;

    private void testRequestPOSTWithSSL() {
        String url = "/rest/v1/pipeline/t02e3334bd5-c201-456b-9f6b-efa7a7af8d8d/start";
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
//        paramList.add(new BasicNameValuePair("rev", "0"));
        paramList.add(new BasicNameValuePair("runtimeParameters", "{}"));
        String retVal = DistributionHttpClient.requestPOSTWithSSL(url, host, port, username,
                password, paramList);
        System.out.println("--->>> retVal: " + retVal);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Test test = new Test();

        test.testRequestPOSTWithSSL();
    }

}
