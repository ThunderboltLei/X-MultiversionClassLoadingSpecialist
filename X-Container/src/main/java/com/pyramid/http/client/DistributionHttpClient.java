package com.pyramid.http.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author thunderbolt.lei <br>
 * 
 *         jetty client for distribution<br>
 *
 *
 *         <dependency><br/>
 *         <groupId>org.eclipse.jetty</groupId><br/>
 *         <artifactId>jetty-client</artifactId><br/>
 *         <version>9.4.8.v20180619</version><br/>
 *         </dependency><br/>
 *         <dependency><br/>
 *         <groupId>org.apache.httpcomponents</groupId><br/>
 *         <artifactId>httpclient</artifactId><br/>
 *         <version>4.5.6</version><br/>
 *         </dependency><br/>
 */
public class DistributionHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(DistributionHttpClient.class);

    private static final Integer CONNECT_TIME_OUT = 5000;

    // Not allow to construct
    private DistributionHttpClient() {
    }

    // @Deprecated
    // public static ContentResponse requestPOST(String url) throws Exception {
    // HttpClient client = new HttpClient();
    // client.setConnectTimeout(CONNECT_TIME_OUT);
    // client.setFollowRedirects(false);
    // client.start();
    // ContentResponse response = client.POST(url).send();
    // client.stop();
    // return response;
    // }

    // @Deprecated
    // public static ContentResponse requestPOST(String url, Map<String, String>
    // headerMap,
    // Map<String, String> paramMap) throws Exception {
    // HttpClient client = new HttpClient();
    // client.setConnectTimeout(CONNECT_TIME_OUT);
    // client.setFollowRedirects(false);
    // client.start();
    // Request request = client.POST(url);
    // if (MapUtils.isNotEmpty(headerMap)) {
    // for (Map.Entry<String, String> x : headerMap.entrySet()) {
    // request = request.header(x.getKey(), x.getValue());
    // }
    // }
    // request = request.accept("application/json", "text/plain", "*/*");
    // if (MapUtils.isNotEmpty(paramMap)) {
    // for (Map.Entry<String, String> x : paramMap.entrySet()) {
    // request = request.param(x.getKey(), x.getValue());
    // }
    // }
    // LOG.info("--->>> DistributionHttpClient - Request: {}",
    // JSON.toJSONString(request));
    // ContentResponse response = request.send();
    // client.stop();
    // return response;
    // }

    // @Deprecated
    // public static ContentResponse requestGET(String url) throws Exception {
    // HttpClient client = new HttpClient();
    // client.setConnectTimeout(CONNECT_TIME_OUT);
    // client.setFollowRedirects(false);
    // client.start();
    // Request request = client.newRequest(url);
    // sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    // String authorization = "Basic" +
    // decoder.decodeBufferToByteBuffer("admin:admin");
    // LOG.info("--->>> authorization: {}", authorization);
    // request = request.header("Authorization", authorization);
    // request = request.header("admin", "admin");
    // LOG.info("--->>> request: {}", JSON.toJSONString(request));
    // ContentResponse response = request.send();
    // client.stop();
    // return response;
    // }

    // @Deprecated
    // public static ContentResponse requestGET02(String url) throws Exception {
    // HttpClient client = new HttpClient();
    // AuthenticationStore auth = client.getAuthenticationStore();
    // auth.addAuthentication(new BasicAuthentication(new URI(url), "", "admin",
    // "admin"));
    // ContentResponse response = client.newRequest(url).send();
    // return response;
    // }

    // ----- NO SSL ----- //
    public static String requestDELETEWithNoSSL(String url, String host, int port, String username,
            String password, List<NameValuePair> paramList) {
        try {
            String strParams = EntityUtils
                    .toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            HttpDelete httpDelete = new HttpDelete(url + "?" + strParams);
            return requestWithNoSSL(host, port, username, password, httpDelete);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String requestPUTWithNoSSL(String url, String host, int port, String username,
            String password, List<NameValuePair> paramList) {
        try {
            HttpPut httpPut = new HttpPut(url);
            httpPut.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            return requestWithNoSSL(host, port, username, password, httpPut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String requestGETWithNoSSL(String url, String host, int port, String username,
            String password, List<NameValuePair> paramList) {
        try {
            String strParams = EntityUtils
                    .toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            HttpGet httpGet = new HttpGet(url + "?" + strParams);
            return requestWithNoSSL(host, port, username, password, httpGet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String requestPOSTWithNoSSL(String url, String host, int port, String username,
            String password, List<NameValuePair> paramList) {
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            return requestWithNoSSL(host, port, username, password, httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String requestWithNoSSL(String host, int port, String username, String password,
            HttpUriRequest request) {
        // return value
        Map<String, Object> retMap = new HashMap<String, Object>();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        // HttpGet httpget = new HttpGet(url); // This is the child class of
        // HttpRequest.
        try {
            CloseableHttpResponse response = httpclient.execute(request);
            // LOG.info("--->>> CloseableHttpResponse: {}", JSON.toJSONString(response));
            try {
                HttpEntity entity = response.getEntity();
                // LOG.info("--->>> HttpEntity: {}", JSON.toJSONString(entity));
                // LOG.info("--->>> Content: {}", EntityUtils.toString(entity, "UTF-8"));
                StatusLine statusLine = response.getStatusLine();
                // LOG.info("--->>> StatusLine: {}", JSON.toJSONString(statusLine));
                if (statusLine.getStatusCode() == 200) {
                    retMap.put("status", statusLine.getStatusCode());
                    retMap.put("message", EntityUtils.toString(entity, "UTF-8"));
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            retMap.put("status", 404);
            retMap.put("message", e);
        }

        return JSON.toJSONString(retMap);
    }

    // ----- SSL ----- //
    public static String requestDELETEWithSSL(String url, String host, int port, String username,
            String password, List<NameValuePair> paramList) {
        try {
            String strParams = EntityUtils
                    .toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            HttpDelete httpDelete = new HttpDelete(url + "?" + strParams);
            return requestWithSSL(host, port, username, password, httpDelete);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String requestPUTWithSSL(String url, String host, int port, String username,
            String password, List<NameValuePair> paramList) {
        try {
            HttpPut httpPut = new HttpPut(url);
            httpPut.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            return requestWithSSL(host, port, username, password, httpPut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String requestGETWithSSL(String url, String host, int port, String username,
            String password, List<NameValuePair> paramList) {
        try {
            String strParams = EntityUtils
                    .toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            HttpGet httpGet = new HttpGet(url + "?" + strParams);
            return requestWithSSL(host, port, username, password, httpGet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String requestPOSTWithSSL(String url, String host, int port, String username,
            String password, List<NameValuePair> paramList) {

        URIBuilder builder = new URIBuilder();
        URI uri = null;
        try {
            uri = builder.setScheme("http").setHost(host + ":" + port).setPath(url).build();
        } catch (URISyntaxException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(uri.toString());
        httpPost.setHeader("Content-Type", "application/json");
//        httpPost.setHeader("access-control-allow-credentials", "true");
//        httpPost.setHeader("access-control-allow-origin", "http://" + host + ":" + port);
        try {
            if (CollectionUtils.isNotEmpty(paramList)) {
                httpPost.setParams(new BasicHttpParams().setParameter("rec", 0));
                httpPost.setEntity(new StringEntity(JSON.toJSONString(paramList),
                        ContentType.create("application/json", "utf-8")));
                System.out.println(
                        "request parameters: " + EntityUtils.toString(httpPost.getEntity()));
            } else {
                httpPost.setEntity(
                        new StringEntity("", ContentType.create("application/json", "utf-8")));
            }
            return requestWithSSL(host, port, username, password, httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String requestWithSSL(String host, int port, String username, String password,
            HttpRequest request) {
        // return value
        Map<String, Object> retMap = new HashMap<String, Object>();

        HttpHost targetHost = new HttpHost(host, port, "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(username, password));

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpclient.execute(targetHost, request, context);
            LOG.info("--->>> CloseableHttpResponse: {}", JSON.toJSONString(response));
            try {
                HttpEntity entity = response.getEntity();
                LOG.info("--->>> HttpEntity: {}", JSON.toJSONString(entity));
                LOG.info("--->>> Content: {}", EntityUtils.toString(entity, "UTF-8"));
                StatusLine statusLine = response.getStatusLine();
                LOG.info("--->>> StatusLine: {}", JSON.toJSONString(statusLine));
                if (statusLine.getStatusCode() == 200) {
                    retMap.put("status", statusLine.getStatusCode());
                    retMap.put("message", EntityUtils.toString(entity, "UTF-8"));
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            retMap.put("status", 404);
            retMap.put("message", e);
        }

        return JSON.toJSONString(retMap);
    }

}
