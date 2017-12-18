package com.it18zhang.umeng.util;

import com.it18zhang.umeng.common.*;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenLogUtil {

    /**
     * 根据日志类型，返回日志对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T genLog(Class<T> clazz) {
        try {
            T t1 = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Class fType = field.getType();
                String fName = field.getName();
                if (fType == String.class) {
                    String value = DictionaryUtil.randomValue(fName.toLowerCase());
                    field.set(t1, value);
                }
            }
            //如果t1是base的实例，则设置创建时间
            if(t1 instanceof AppBaseLog){
                ((AppBaseLog)t1).setCreatedAtMs(System.currentTimeMillis());
            }
            //如果是日志聚合体，则设置设备id
            if(t1 instanceof AppLogAggEntity){
                Random r = new Random();
                int i = r.nextInt(1000);
                DecimalFormat df = new DecimalFormat("0000");
                String deviceId ="d" + df.format(i);
                ((AppLogAggEntity)t1).setDeviceId(deviceId);
            }
            return t1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据日志类型，返回list类型
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> genLogList(Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        int n = new Random().nextInt(10) + 1;
        for (int i = 0; i < n; i++) {
            list.add(genLog(clazz));
        }
        return list;
    }

    public static AppLogAggEntity genLogAgg() {
        AppLogAggEntity agg = genLog(AppLogAggEntity.class);

        agg.setAppErrorLogs(genLogList(AppErrorLog.class));
        agg.setAppEventLogs(genLogList(AppEventLog.class));
        agg.setAppStartupLogs(genLogList(AppStartupLog.class));
        agg.setAppUsageLogs(genLogList(AppUsageLog.class));
        agg.setAppPageLogs(genLogList(AppPageLog.class));
        return agg;
    }

}
