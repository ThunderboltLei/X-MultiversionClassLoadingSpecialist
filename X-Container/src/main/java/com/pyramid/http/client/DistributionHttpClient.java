package com.pyramid.http.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pyramid.consts.DConsts;

/**
 * @author thunderbolt.lei <br>
 * 
 *         jetty client for distribution<br>
 */
public class DistributionHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(DistributionHttpClient.class);

    // Not allow to construct
    private DistributionHttpClient() {
    }

    // ----- NO SSL ----- //
    public static Map<String, Object> requestDELETEWithNoSSL(String url,
            List<NameValuePair> paramList) {
        try {
            HttpDelete httpDelete = null;
            if (CollectionUtils.isEmpty(paramList)) {
                httpDelete = new HttpDelete(url);
            } else {
                String strParams = EntityUtils
                        .toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
                httpDelete = new HttpDelete(url + "?" + strParams);
            }
            // request config
            httpDelete.setConfig(initRequestConfig());
            return requestWithNoSSL(httpDelete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> requestPUTWithNoSSL(String url,
            List<NameValuePair> paramList) {
        try {
            HttpPut httpPut = new HttpPut(url);
            // request config
            httpPut.setConfig(initRequestConfig());
            // header
            httpPut.addHeader("Accept", "application/json, text/plain, */*");
            httpPut.addHeader("Content-Type", MediaType.APPLICATION_JSON);
            httpPut.addHeader("Accept-Encoding", "gzip, deflate");
            httpPut.addHeader("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/11.0.1 Safari/604.3.5");
            httpPut.addHeader("Cookie",
                    "NG_TRANSLATE_LANG_KEY=zh; _ga=GA1.1.349586053.1533623958; _gid=GA1.1.6885633.1534214544; _gat=1; JSESSIONID_18681=node0sohcqg0de1r913cj4hoam9xy11.node0");
            httpPut.addHeader("X-Requested-By", "Data Collector"); // maybe could be anything else
            // entity
            if (CollectionUtils.isNotEmpty(paramList)) {
                httpPut.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            }
            return requestWithNoSSL(httpPut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> requestGETWithNoSSL(String url,
            List<NameValuePair> paramList) {
        try {
            HttpGet httpGet = null;
            if (CollectionUtils.isEmpty(paramList)) {
                httpGet = new HttpGet(url);
            } else {
                String strParams = EntityUtils
                        .toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
                httpGet = new HttpGet(url + "?" + strParams);
            }
            // request config
            httpGet.setConfig(initRequestConfig());
            return requestWithNoSSL(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> requestPOSTWithNoSSL(String url,
            List<NameValuePair> paramList) {
        try {
            HttpPost httpPost = new HttpPost(url);
            // request config
            httpPost.setConfig(initRequestConfig());
            // header
            httpPost.addHeader("Accept", "application/json, text/plain, */*");
            httpPost.addHeader("Content-Type", MediaType.APPLICATION_JSON);
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/11.0.1 Safari/604.3.5");
            httpPost.addHeader("Cookie",
                    "NG_TRANSLATE_LANG_KEY=zh; _ga=GA1.1.349586053.1533623958; _gid=GA1.1.6885633.1534214544; _gat=1; JSESSIONID_18681=node0sohcqg0de1r913cj4hoam9xy11.node0");
            httpPost.addHeader("X-Requested-By", "Data Collector"); // maybe could be anything else
            // entity
            if (CollectionUtils.isNotEmpty(paramList)) {
                httpPost.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            }
            return requestWithNoSSL(httpPost);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Object> requestWithNoSSL(HttpUriRequest request) {
        // return value
        JSONObject retMap = new JSONObject();

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            StatusLine statusLine = response.getStatusLine();
            String message = EntityUtils.toString(entity, Consts.UTF_8);
            if (statusLine.getStatusCode() == 200) {
                retMap.put("status", statusLine.getStatusCode());
                retMap.put("message", JSON.parseObject(message));
            }
        } catch (Exception e) {
            retMap.put("status", 404);
            retMap.put("message", e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
                if (null != httpclient) {
                    httpclient.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        LOG.info("--->>> DistributionHttpClient - requestWithNoSSL - retMap: {}",
                retMap.toJSONString());

        return retMap;
    }

    // ----- SSL ----- //
    public static Map<String, Object> requestDELETEWithSSL(String url, String host, int port,
            String username, String password, List<NameValuePair> paramList) {
        try {
            HttpDelete httpDelete = null;
            if (CollectionUtils.isEmpty(paramList)) {
                httpDelete = new HttpDelete(url);
            } else {
                String strParams = EntityUtils
                        .toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
                httpDelete = new HttpDelete(url + "?" + strParams);
            }
            // request config
            httpDelete.setConfig(initRequestConfig());
            return requestWithSSL(host, port, username, password, httpDelete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> requestPUTWithSSL(String url, String host, int port,
            String username, String password, List<NameValuePair> paramList) {
        try {
            HttpPut httpPut = new HttpPut(url);
            // request config
            httpPut.setConfig(initRequestConfig());
            // header
            httpPut.addHeader("Accept", "application/json, text/plain, */*");
            httpPut.addHeader("Content-Type", MediaType.APPLICATION_JSON);
            httpPut.addHeader("Accept-Encoding", "gzip, deflate");
            httpPut.addHeader("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/11.0.1 Safari/604.3.5");
            httpPut.addHeader("Cookie",
                    "NG_TRANSLATE_LANG_KEY=zh; _ga=GA1.1.349586053.1533623958; _gid=GA1.1.6885633.1534214544; _gat=1; JSESSIONID_18681=node0sohcqg0de1r913cj4hoam9xy11.node0");
            httpPut.addHeader("X-Requested-By", "Data Collector"); // maybe could be anything else
            // entity
            if (CollectionUtils.isNotEmpty(paramList)) {
                httpPut.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
                // httpPut.setEntity(new StringEntity(JSON.toJSONString(paramList),
                // Consts.UTF_8));
                LOG.info("--->>> requestPOSTWithSSL - request url: "
                        + JSON.toJSONString(httpPut.getURI()));
                LOG.info("--->>> requestPOSTWithSSL - request parameters: "
                        + EntityUtils.toString(httpPut.getEntity()));
            }
            return requestWithSSL(host, port, username, password, httpPut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> requestGETWithSSL(String url, String host, int port,
            String username, String password, List<NameValuePair> paramList) {
        try {
            HttpGet httpGet = null;
            if (CollectionUtils.isEmpty(paramList)) {
                httpGet = new HttpGet(url);
            } else {
                String strParams = EntityUtils
                        .toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
                httpGet = new HttpGet(url + "?" + strParams);
            }
            // request config
            httpGet.setConfig(initRequestConfig());
            return requestWithSSL(host, port, username, password, httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> requestPOSTWithSSL(String url, String host, int port,
            String username, String password, List<NameValuePair> paramList) {
        HttpPost httpPost = new HttpPost(url);
        // request config
        httpPost.setConfig(initRequestConfig());
        // header
        httpPost.addHeader("Accept", "application/json, text/plain, */*");
        httpPost.addHeader("Content-Type", MediaType.APPLICATION_JSON);
        httpPost.addHeader("Accept-Encoding", "gzip, deflate");
        httpPost.addHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/11.0.1 Safari/604.3.5");
        httpPost.addHeader("Cookie",
                "NG_TRANSLATE_LANG_KEY=zh; _ga=GA1.1.349586053.1533623958; _gid=GA1.1.6885633.1534214544; _gat=1; JSESSIONID_18681=node0sohcqg0de1r913cj4hoam9xy11.node0");
        httpPost.addHeader("X-Requested-By", "Data Collector"); // maybe could be anything else
        // entity
        try {
            if (CollectionUtils.isNotEmpty(paramList)) {
                httpPost.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
                httpPost.setEntity(new StringEntity(JSON.toJSONString("")));
                // httpPost.setEntity(new StringEntity(JSON.toJSONString(paramList),
                // Consts.UTF_8));
                LOG.info("--->>> requestPOSTWithSSL - request url: "
                        + JSON.toJSONString(httpPost.getURI()));
                LOG.info("--->>> requestPOSTWithSSL - request parameters: "
                        + EntityUtils.toString(httpPost.getEntity()));
            }
            return requestWithSSL(host, port, username, password, httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Object> requestWithSSL(String host, int port, String username,
            String password, HttpUriRequest request) {
        LOG.info("--->>> requestWithSSL - request: {}", JSON.toJSONString(request));

        // return value
        JSONObject retMap = new JSONObject();

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            HttpHost targetHost = new HttpHost(host, port, "http");
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(targetHost.getHostName(), targetHost.getPort()),
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
            context.setTargetHost(targetHost);

            httpclient = HttpClients.createDefault();
            response = httpclient.execute(request, context);
            LOG.info("--->>> CloseableHttpResponse: {}", JSON.toJSONString(response));
            HttpEntity entity = response.getEntity();
            LOG.info("--->>> HttpEntity: {}", JSON.toJSONString(entity));
            String message = EntityUtils.toString(entity, Consts.UTF_8);
            LOG.info("--->>> Content: {}", message);
            StatusLine statusLine = response.getStatusLine();
            LOG.info("--->>> StatusLine: {}", JSON.toJSONString(statusLine));
            if (statusLine.getStatusCode() == 200 || statusLine.getStatusCode() == 201) {
                retMap.put("status", statusLine.getStatusCode());
                retMap.put("message", JSON.parseObject(message));
            }
            // response.close();
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("status", 404);
            retMap.put("message", e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpclient) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return retMap;
    }

    /**
     * initialize the request configuration
     * 
     * @return
     */
    private static RequestConfig initRequestConfig() {
        return RequestConfig.custom() // RequestConfig.custom
                .setConnectTimeout(DConsts.DISTRIBUTION_HTTP_TIMEOUT) // connection timeout
                .build();
    }

}