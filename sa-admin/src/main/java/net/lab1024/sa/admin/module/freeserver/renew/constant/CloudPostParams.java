package net.lab1024.sa.admin.module.freeserver.renew.constant;

import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Demo-Liu
 * @create 2020-08-03 11:17
 * @description 接口参数
 */
public class CloudPostParams {

    private CloudPostParams() {
    }

    /**
     * 获取云登录参数
     * @param username 账号
     * @param password 密码
     */
    public static UrlEncodedFormEntity getYunLogin(String username, String password) {
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("cmd", "login"));
        list.add(new BasicNameValuePair("id_mobile", username));
        list.add(new BasicNameValuePair("password", password));
        return new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
    }

    /**
     * 获取查询服务器状态参数
     */
    public static UrlEncodedFormEntity getFreeStatus() {
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("cmd", "check_free_delay"));
        list.add(new BasicNameValuePair("ptype", "vps"));
        return new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
    }

    /**
     * 获取审核状态参数
     */
    public static UrlEncodedFormEntity getCheckStatus() {
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("cmd", "free_delay_list"));
        list.add(new BasicNameValuePair("ptype", "vps"));
        list.add(new BasicNameValuePair("count", "20"));
        list.add(new BasicNameValuePair("page", "1"));
        return new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
    }

    /**
     * 延期博客参数
     * @param cloudInfo 服务器信息
     * @param file 截图文件路径
     * @param blogUrl 博客url
     */
    public static MultipartEntityBuilder getBlogInfo(CloudInfo cloudInfo, File file, String blogUrl) {
        MultipartEntityBuilder meb = MultipartEntityBuilder.create();

        if (cloudInfo == CloudInfo.ABEI) {
            meb.addBinaryBody("yanqi_img", file);
        } else {
            meb.addBinaryBody("yanqi_img", file, ContentType.IMAGE_PNG, "postpone.png");
        }

        meb.setCharset(StandardCharsets.UTF_8);
        ContentType contentType = ContentType.create("text/plain", StandardCharsets.UTF_8);

        meb.addTextBody("cmd", "free_delay_add", contentType);
        meb.addTextBody("ptype", "vps", contentType);
        meb.addTextBody("url", blogUrl, contentType);
        return meb;
    }

    /**
     * 获取公共请求头Map
     */
    public static Map<String, String> getPubHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("Referer", "https://www.baidu.com/");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        map.put("Accept-Encoding", "gzip, deflate, br");
        map.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        return map;
    }
}