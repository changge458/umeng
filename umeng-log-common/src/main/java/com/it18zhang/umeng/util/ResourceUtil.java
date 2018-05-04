package com.it18zhang.umeng.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ResourceUtil {

    /**
     * 读取文件为string
     * @param path
     * @return
     */
    public static String readResourceFile(String path){
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if(in == null){
            in = ResourceUtil.class.getResourceAsStream(path);
        }
        return readInputStreamAsString(in);
    }

    public static String readInputStreamAsString(InputStream is){

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len = -1;
            while((len = is.read(buf)) != -1){
                baos.write(buf,0,len);
            }
            is.close();
            return new String(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
