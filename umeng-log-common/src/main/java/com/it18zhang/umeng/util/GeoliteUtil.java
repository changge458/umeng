package com.it18zhang.umeng.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.db.Reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class GeoliteUtil {

    //
    private static Reader reader ;

    //
    private static Map<String,String> cache = new HashMap<String, String>() ;

    static{
        try {
            InputStream in = GeoliteUtil.class.getResourceAsStream("GeoLite2-City.mmdb");
            reader = new Reader(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String processCache(String ip){
        String value = cache.get(ip) ;
        if(value == null){
            try {
                JsonNode node = reader.get(InetAddress.getByName(ip)) ;
                String country = "unknown" ;
                String prov = "unknown" ;
                if(node != null){
                    try {
                        country = node.get("country").get("names").get("zh-CN").textValue();
                    }
                    catch (Exception e) {
                    }
                    try {
                        prov = node.get("subdivisions").get(0).get("names").get("zh-CN").textValue();

                    } catch (Exception e) {
                    }
                }
                cache.put(ip, country + "," + prov);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cache.get(ip) ;
    }

    public static String getCountry(String ip){
        return processCache(ip).split(",")[0];
    }
    public static String getProvince(String ip){
        return processCache(ip).split(",")[1];
    }
}
