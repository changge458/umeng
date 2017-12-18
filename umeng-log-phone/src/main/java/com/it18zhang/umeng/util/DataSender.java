package com.it18zhang.umeng.util;

import com.alibaba.fastjson.JSON;
import com.it18zhang.umeng.common.AppLogAggEntity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataSender {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i <5; i++) {
            AppLogAggEntity aggLog = GenLogUtil.genLogAgg();
            String json = JSON.toJSONString(aggLog,false);
            doSend(json);
            System.out.println(json);
            Thread.sleep(1000);
        }
    }

    /**
     * 执行发送
     * @param json
     */
    private static void doSend(String json) {
        try {
            String strURL = "http://localhost:8080/collector/agg";
            URL url = new URL(strURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            conn.setRequestMethod("POST");
            //允许输出到服务器
            conn.setDoOutput(true);
            //设置上传数据的内容类型
            conn.setRequestProperty("content-Type","application/json");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
            System.out.println(conn.getResponseCode());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
