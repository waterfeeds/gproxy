package com.waterfeeds.gproxy.protocol.base;

import com.alibaba.fastjson.JSONObject;
import com.waterfeeds.gproxy.message.Const;

public class JsonBuf {
    public static String getClientId(String content) {
        JSONObject object = JSONObject.parseObject(content);
        return (String) object.get(Const.CLIENT_ID);
    }

    public static String getUserId(String content) {
        JSONObject object = JSONObject.parseObject(content);
        return (String) object.get(Const.USER_ID);
    }

    public static String getGroupId(String content) {
        JSONObject object = JSONObject.parseObject(content);
        return (String) object.get(Const.GROUP_ID);
    }

    public static String getMessage(String content) {
        JSONObject object = JSONObject.parseObject(content);
        return (String) object.get(Const.MESSAGE);
    }

    public static String getJsonByClientId(String clientId, String message) {
        JSONObject object = new JSONObject();
        object.put(Const.CLIENT_ID, clientId);
        object.put(Const.MESSAGE, message);
        return object.toString();
    }

    public static String getJsonByUserId(String userId, String message) {
        JSONObject object = new JSONObject();
        object.put(Const.USER_ID, userId);
        object.put(Const.MESSAGE, message);
        return object.toString();
    }

    public static String getJsonByGroupId(String groupId, String message) {
        JSONObject object = new JSONObject();
        object.put(Const.GROUP_ID, groupId);
        object.put(Const.MESSAGE, message);
        return object.toString();
    }
}
