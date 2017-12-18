package com.it18zhang.umeng.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class DictionaryUtil {

    //数据字典集合
    private static Map<String, List<String>> dict = new HashMap<String, List<String>>();

    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream is = loader.getResourceAsStream("dictionary.dat");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            List<String> values = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("[")) {
                    String key = line.substring(1, line.length() - 1);
                    values = new ArrayList<String>();
                    dict.put(key, values);

                } else {
                    values.add(line);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //随机提取指定value的值
    public static String randomValue(String key){
        Random r = new Random();
        List<String> values = dict.get(key);
        if(values == null){
            return null;
        }
        return values.get(r.nextInt(values.size()));

    }




}
