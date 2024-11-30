package wkz.org.backend.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public class NetworkUtil {
    public static String response(String code, String message, Object data) {
        JSONObject response = new JSONObject();
        response.put("code", code);
        response.put("message", message);
        response.put("data", data);
        return JSON.toJSONString(response);
    }

    public static String response(String code, String message) {
        JSONObject response = new JSONObject();
        response.put("code", code);
        response.put("message", message);
        return JSON.toJSONString(response);
    }

    public static String response(String message) {
        return response("ok", message);
    }

    // 返回用户权限
    public static String response(String code, String message, String role) {
        JSONObject response = new JSONObject();
        response.put("code", code);
        response.put("message", message);
        response.put("role", role);
        return JSON.toJSONString(response);
    }
}
