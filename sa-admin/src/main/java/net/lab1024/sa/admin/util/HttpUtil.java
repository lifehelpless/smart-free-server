package net.lab1024.sa.admin.util;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP 工具类
 */
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static final String COOKIE_SESSION_ID = "session_id";
    public static final String COOKIE_ACW_TC = "acw_tc";
    public static final String COOKIE_CDN_SEC_TC = "cdn_sec_tc";

    private HttpUtil() {
    }

    public static HttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    public static HttpClient getHttpClient(BasicCookieStore cookieStore) {
        return HttpClients.custom()
                .disableCookieManagement()
                .build();
    }

    public static String getPostRes(HttpClient client, String url, NameValuePair[] pair,
                                    Map<String, String> headerMap) throws IOException, URISyntaxException {
        HttpPost httpPost;
        if (pair != null && pair.length > 0) {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setParameters(pair);
            httpPost = new HttpPost(uriBuilder.build());
        } else {
            httpPost = new HttpPost(url);
        }
        return getPostRes(client, httpPost, headerMap);
    }

    public static String getPostRes(HttpClient client, String url, NameValuePair[] pair)
            throws IOException, URISyntaxException {
        return getPostRes(client, url, pair, null);
    }

    public static String getPostRes(HttpClient client, String url, HttpEntity entity,
                                    Map<String, String> headerMap) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return getPostRes(client, httpPost, headerMap);
    }

    public static String getPostRes(HttpClient client, String url, HttpEntity entity) throws IOException {
        return getPostRes(client, url, entity, null);
    }

    public static String getPostResAndCaptureCookies(HttpClient client, String url, NameValuePair[] pair,
                                                     Map<String, String> headerMap,
                                                     Map<String, String> cookieMap)
            throws IOException, URISyntaxException {
        HttpPost httpPost;
        if (pair != null && pair.length > 0) {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setParameters(pair);
            httpPost = new HttpPost(uriBuilder.build());
        } else {
            httpPost = new HttpPost(url);
        }
        return getPostResAndCaptureCookies(client, httpPost, headerMap, cookieMap);
    }

    public static String getPostResAndCaptureCookies(HttpClient client, String url, HttpEntity entity,
                                                     Map<String, String> headerMap,
                                                     Map<String, String> cookieMap) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return getPostResAndCaptureCookies(client, httpPost, headerMap, cookieMap);
    }

    private static String getPostResAndCaptureCookies(HttpClient client, HttpPost httpPost,
                                                      Map<String, String> headerMap,
                                                      Map<String, String> cookieMap) throws IOException {
        setHeaders(httpPost, headerMap);

        String res = client.execute(httpPost, response -> {
            if (cookieMap != null) {
                mergeCookies(cookieMap, extractCookies(response));
                logCookieSnapshot(cookieMap);
            }
            if (response.getEntity() == null) {
                return null;
            }
            return EntityUtils.toString(response.getEntity());
        });
        return removeBom(res);
    }

    private static String getPostRes(HttpClient client, HttpPost httpPost,
                                     Map<String, String> headerMap) throws IOException {
        setHeaders(httpPost, headerMap);

        String res = client.execute(httpPost, response -> {
            if (response.getEntity() == null) {
                return null;
            }
            return EntityUtils.toString(response.getEntity());
        });

        try {
            log.info("getPostRes=>{{{}}}", httpPost.getUri(), res);
        } catch (URISyntaxException e) {
            log.warn("URI 语法异常", e);
        }
        return removeBom(res);
    }

    public static String getGetRes(HttpClient client, String url, NameValuePair[] pair,
                                   Map<String, String> headerMap) throws IOException, URISyntaxException {

        HttpGet httpGet;
        if (pair != null && pair.length > 0) {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setParameters(pair);
            httpGet = new HttpGet(uriBuilder.build());
        } else {
            httpGet = new HttpGet(url);
        }

        setHeaders(httpGet, headerMap);

        String res = client.execute(httpGet, response -> {
            if (response.getEntity() == null) {
                return null;
            }
            return EntityUtils.toString(response.getEntity());
        });

        return removeBom(res);
    }

    public static String getGetRes(HttpClient client, String url, NameValuePair[] pair)
            throws IOException, URISyntaxException {
        return getGetRes(client, url, pair, null);
    }

    private static void setHeaders(HttpMessage message, Map<String, String> headerMap) {
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                message.setHeader(entry.getKey(), entry.getValue());
                log.debug("设置请求头：{}={}", entry.getKey(), entry.getValue());
            }
        }
    }

    public static Map<String, String> extractCookies(HttpResponse response) {
        Map<String, String> cookies = new LinkedHashMap<>();
        if (response == null) {
            return cookies;
        }

        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header header : headers) {
            String value = header.getValue();
            if (value == null || value.isEmpty()) {
                continue;
            }
            int index = value.indexOf(';');
            String cookiePair = index >= 0 ? value.substring(0, index) : value;
            int eqIndex = cookiePair.indexOf('=');
            if (eqIndex <= 0) {
                continue;
            }
            String name = cookiePair.substring(0, eqIndex).trim();
            String cookieValue = cookiePair.substring(eqIndex + 1).trim();
            if (!name.isEmpty() && !cookieValue.isEmpty()) {
                cookies.put(name, cookieValue);
            }
        }
        return cookies;
    }

    public static void mergeCookies(Map<String, String> target, Map<String, String> source) {
        if (target == null || source == null || source.isEmpty()) {
            return;
        }
        target.putAll(source);
    }

    public static String buildCookieHeader(Map<String, String> cookieMap) {
        if (cookieMap == null || cookieMap.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        appendCookie(builder, COOKIE_SESSION_ID, cookieMap.get(COOKIE_SESSION_ID));
        appendCookie(builder, COOKIE_ACW_TC, cookieMap.get(COOKIE_ACW_TC));
        appendCookie(builder, COOKIE_CDN_SEC_TC, cookieMap.get(COOKIE_CDN_SEC_TC));

        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            String key = entry.getKey();
            if (COOKIE_SESSION_ID.equals(key) || COOKIE_ACW_TC.equals(key) || COOKIE_CDN_SEC_TC.equals(key)) {
                continue;
            }
            appendCookie(builder, key, entry.getValue());
        }

        return builder.length() == 0 ? null : builder.toString();
    }

    public static Map<String, String> buildHeadersWithCookies(Map<String, String> headerMap,
                                                              Map<String, String> cookieMap) {
        Map<String, String> headers = headerMap == null ? new HashMap<>() : new HashMap<>(headerMap);
        String cookieHeader = buildCookieHeader(cookieMap);
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            headers.put("Cookie", cookieHeader);
        }
        return headers;
    }

    public static boolean hasEssentialCookies(Map<String, String> cookieMap) {
        return cookieMap != null
                && cookieMap.containsKey(COOKIE_SESSION_ID)
                && cookieMap.containsKey(COOKIE_ACW_TC)
                && cookieMap.containsKey(COOKIE_CDN_SEC_TC);
    }

    private static void appendCookie(StringBuilder builder, String name, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        if (builder.length() > 0) {
            builder.append("; ");
        }
        builder.append(name).append("=").append(value);
    }

    private static void logCookieSnapshot(Map<String, String> cookieMap) {
        if (cookieMap == null || cookieMap.isEmpty()) {
            return;
        }
        log.info("当前 Cookie: {}", buildCookieHeader(cookieMap));
    }

    private static String removeBom(String res) {
        if (res != null && res.startsWith("\uFEFF")) {
            log.debug("检测到 BOM 标记，已移除");
            return res.substring(1);
        }
        return res;
    }

    public static HttpResponse executePostWithResponse(HttpClient client, String url,
                                                       HttpEntity entity, Map<String, String> headerMap)
            throws IOException {
        HttpPost httpPost = new HttpPost(url);

        if (entity != null) {
            httpPost.setEntity(entity);
        }

        setHeaders(httpPost, headerMap);

        log.info("执行 POST 请求：{}", url);
        return client.execute(httpPost, response -> response);
    }

    public static String executePostWithCookie(BasicCookieStore cookieStore, HttpClient client,
                                               String url, HttpEntity entity, Map<String, String> headerMap)
            throws IOException {

        if (cookieStore != null && !cookieStore.getCookies().isEmpty()) {
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }

            StringBuilder cookieBuilder = new StringBuilder();
            cookieStore.getCookies().forEach(cookie -> {
                if (cookieBuilder.length() > 0) {
                    cookieBuilder.append("; ");
                }
                cookieBuilder.append(cookie.getName()).append("=").append(cookie.getValue());
            });

            headerMap.put("Cookie", cookieBuilder.toString());
            log.debug("自动添加 Cookie 到请求头");
        }

        return getPostRes(client, url, entity, headerMap);
    }
}
