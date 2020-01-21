package org.cmk;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by changmk on 2020/1/17
 **/
public class Test {

    public static void main(String[] args) {
        String string = "{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"code\":\"502000\",\"602001\":\"25\",\"602004\":\"302000\"}";

        JSONObject jsonObject = JSONObject.parseObject(string);
        System.out.println(jsonObject.toJSONString());
    }

}
