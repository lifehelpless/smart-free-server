package net.lab1024.sa.admin.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON 工具类，统一封装 fastjson2 的常用读写操作。
 */
public final class JSONUtils {

    private static final Logger log = LoggerFactory.getLogger(JSONUtils.class);

    private JSONUtils() {
    }

    public static JSONObject parseObject(String text) {
        if (StringUtil.isEmpty(text)) {
            return new JSONObject();
        }
        try {
            return JSONObject.parseObject(text);
        } catch (Exception e) {
            log.error("JSON 解析对象失败，原始内容:{}", text, e);
            return new JSONObject();
        }
    }

    public static JSONArray parseArray(String text) {
        if (StringUtil.isEmpty(text)) {
            return new JSONArray();
        }
        try {
            return JSONArray.parseArray(text);
        } catch (Exception e) {
            log.error("JSON 解析数组失败，原始内容:{}", text, e);
            return new JSONArray();
        }
    }

    public static JSONObject getObject(JSONObject json, String key) {
        if (json == null || StringUtil.isEmpty(key)) {
            return new JSONObject();
        }
        JSONObject value = json.getJSONObject(key);
        return value == null ? new JSONObject() : value;
    }

    public static JSONArray getArray(JSONObject json, String key) {
        if (json == null || StringUtil.isEmpty(key)) {
            return new JSONArray();
        }
        JSONArray value = json.getJSONArray(key);
        return value == null ? new JSONArray() : value;
    }

    public static String getString(JSONObject json, String key) {
        if (json == null || StringUtil.isEmpty(key)) {
            return null;
        }
        return json.getString(key);
    }

    public static String getString(JSONObject json, String key, String defaultValue) {
        String value = getString(json, key);
        return StringUtil.isEmpty(value) ? defaultValue : value;
    }

    public static boolean containsKey(JSONObject json, String key) {
        return json != null && !StringUtil.isEmpty(key) && json.containsKey(key);
    }

    public static String toJSONString(Object obj) {
        return JSONObject.toJSONString(obj);
    }
}
