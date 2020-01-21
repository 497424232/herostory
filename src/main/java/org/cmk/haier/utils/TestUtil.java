package org.cmk.haier.utils;

import com.alibaba.fastjson.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by changmk on 2020/1/19
 **/
public class TestUtil {

    public static void main(String[] args) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId", "DC330D797381");
        String bodyJson = jsonObject.toString();

        String appId = "MB-UZHSH-0000";
        String appKey = "f50c76fbc8271d361e1f6b5973f54585";
        String appVersion = "99.99.99.99990";
        String token = "TGT1XT7VMBCFCKSH2LVJ8HWIL3T1P0";
        String clientId = "47B1FBE26AEAA4FD8B8C71C2A0D8FF7B";
        String cautionUrl = "/shadow/v1/info";

        HashMap<String, String> header = getHeader(appId, appKey, appVersion, token, clientId, bodyJson, cautionUrl);

        System.out.println(header.get("sign"));

        System.out.println(":" + header.toString());

        System.out.println(bodyJson);
    }

    public static HashMap<String,String> getHeader(String appId, String appKey, String appVersion, String accessToken,
                                                   String clientId, String bodyjson, String url){
        HashMap<String,String> header = new HashMap<>();
        header.put("appId",appId);
        header.put("appVersion",appVersion);
        header.put("timezone","8");
        String timestamp=System.currentTimeMillis()+"";
        System.out.println("timestamp:" + timestamp);
        header.put("timestamp",timestamp);
        header.put("language","zh-cn");
        Random random = new Random();
        String sequenceId = System.currentTimeMillis() + random.nextInt(100) + "";
        header.put("sequenceId",sequenceId);
        header.put("sign",getSign(appId, appKey, timestamp,bodyjson , url));
        header.put("clientId",clientId);
        header.put("accessToken",accessToken);
        header.put("Content-Type","application/json;charset=UTF-8");
        return header;
    }

    public static String getSign(String appId, String appKey, String timestamp, String body,String url){
        appKey = appKey.trim();
        appKey = appKey.replaceAll("\"", "");

        if (body != null) {
            body = body.trim();
        }

        if ((null != body) && (!body.equals(""))) {
            body = body.replaceAll(" ", "");
            body = body.replaceAll("\t", "");
            body = body.replaceAll("\r", "");
            body = body.replaceAll("\n", "");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(url).append(body).append(appId).append(appKey).append(timestamp);
        MessageDigest md = null;
        byte[] bytes = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            bytes = md.digest(sb.toString().getBytes("utf-8"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return BinaryToHexString(bytes);
    }

    public static String BinaryToHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        String hexStr = "0123456789abcdef";
        for (int i = 0; i < bytes.length; i++) {
            hex.append(String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4)));
            hex.append(String.valueOf(hexStr.charAt(bytes[i] & 0x0F)));
        }
        return hex.toString();
    }
}
