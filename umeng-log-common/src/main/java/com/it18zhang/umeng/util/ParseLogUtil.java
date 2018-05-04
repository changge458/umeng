package com.it18zhang.umeng.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseLogUtil {

    public static String parseStartup(String s) {
        return parseJson("appStartupLogs",s);
    }

    public static String parseError(String s) {
        return parseJson("appErrorLogs",s);
    }

    public static String parseEvent(String s) {
        return parseJson("appEventLogs",s);
    }

    public static String parsePage(String s) {
        return parseJson("appPageLogs",s);
    }

    public static String parseUsage(String s) {
        return parseJson("appUsageLogs",s);
    }

    public static String parseChannel(String s) {
        return parseJson("appChannel",s);
    }


    public static String parseJson(String key, String data) {
        String newData = null;
        if (data.contains("\\")){
            newData = data.replaceAll("\\\\","");
            JSONObject jo = JSON.parseObject(newData);
            JSONObject obj = new JSONObject();

            if (jo != null) {
                //通过json对象和key返回jsonArray
                if(key.equals("appChannel")){
                    String val = (String)jo.get(key);
                    obj.put(key,val);

                }
                else {
                    JSONArray array = jo.getJSONArray(key);
                    obj.put(key, array);
                }
            }
            return obj.toJSONString();
        }
        else {
            JSONObject jo = JSON.parseObject(data);
            JSONObject obj = new JSONObject();

            if (jo != null) {
                //通过json对象和key返回jsonArray
                if(key.equals("appChannel")){
                    String val = (String)jo.get(key);
                    obj.put(key,val);

                }
                else {
                    JSONArray array = jo.getJSONArray(key);
                    obj.put(key, array);
                }
            }
            return obj.toJSONString();
        }
    }
}
