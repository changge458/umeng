package com.it18zhang.umeng.util;

import com.alibaba.fastjson.JSON;
import com.it18zhang.umeng.common.AppBaseLog;
import com.it18zhang.umeng.common.AppLogAggEntity;
import com.it18zhang.umeng.common.AppStartupLog;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.JavaStringObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志工具类
 */
public class LogUtil {


    /**
     * 对齐时间
     */
    public static void alignTime(AppLogAggEntity aggLog, long offset) {
        alignTimeLogs(aggLog.getAppErrorLogs(), offset);
        alignTimeLogs(aggLog.getAppEventLogs(), offset);
        alignTimeLogs(aggLog.getAppUsageLogs(), offset);
        alignTimeLogs(aggLog.getAppPageLogs(), offset);
        alignTimeLogs(aggLog.getAppStartupLogs(), offset);
    }

    public static void alignTimeLogs(List<? extends AppBaseLog> logs, long offset) {
        for (AppBaseLog log : logs) {
            log.setCreatedAtMs(log.getCreatedAtMs() + offset);
        }
    }

    public static void saveLogAggToLog(AppLogAggEntity aggLog) {
        saveJsonToLog(aggLog.getAppPageLogs());
        saveJsonToLog(aggLog.getAppUsageLogs());
        saveJsonToLog(aggLog.getAppEventLogs());
        saveJsonToLog(aggLog.getAppErrorLogs());
        saveJsonToLog(aggLog.getAppStartupLogs());
    }

    /**
     * 落地日志文件
     */
    public static void saveJsonToLog(List<? extends AppBaseLog> logs) {
        for (AppBaseLog log : logs) {
            String json = JSON.toJSONString(log, false);
        }
    }


    /**
     * 合并日志基础信息
     */
    public static void mergeBaseInfoToLogs(AppLogAggEntity aggLog) {
        copyProperties2(aggLog, aggLog.getAppStartupLogs());
        copyProperties2(aggLog, aggLog.getAppUsageLogs());
        copyProperties2(aggLog, aggLog.getAppPageLogs());
        copyProperties2(aggLog, aggLog.getAppEventLogs());
        copyProperties2(aggLog, aggLog.getAppErrorLogs());
    }

    /**
     * 复制源对象属性到dest对象，类型一致即可。
     */
    public static void copyProperties(Object src, Object dest) {
        try {
            Class srcClass = src.getClass();
            Class destClass = dest.getClass();
            //源对象
            BeanInfo src_bi = Introspector.getBeanInfo(srcClass);
            BeanInfo dest_bi = Introspector.getBeanInfo(destClass);
            PropertyDescriptor[] src_pps = src_bi.getPropertyDescriptors();
            for (PropertyDescriptor pp : src_pps) {
                Method src_getter = pp.getReadMethod();
                Method src_setter = pp.getWriteMethod();
                if (src_getter != null && src_setter != null) {
                    //得到src的属性名
                    String pname = pp.getName();
                    Class ptype = pp.getPropertyType();
                    src_getter.setAccessible(true);
                    Object value = src_getter.invoke(src);
                    //过滤出String类型属性
                    if (ptype == String.class) {
                        try {
                            Method destSetter = destClass.getMethod(src_setter.getName(), String.class);
                            destSetter.setAccessible(true);
                            destSetter.invoke(dest, value);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyProperties2(Object src, List<? extends AppBaseLog> list) {
        for (Object dest : list) {
            copyProperties(src, dest);
        }
    }

    //获取日志中的所有字段
    public static List<String> fieldsCopy(Class<? extends AppBaseLog> clazz) {
        List<String> list = new ArrayList<String>();
        Class clazz1 = AppLogAggEntity.class;
        Field[] fields = clazz1.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                list.add(field.getName());
            }
        }

        Field[] fields2 = clazz.getDeclaredFields();
        for (Field field2 : fields2) {
            field2.setAccessible(true);
            if (field2.getName() != "serialVersionUID" &&
                    field2.getName() != "country" &&
                    field2.getName() != "province" &&
                    field2.getName() != "ipAddress") {

                list.add(field2.getName());
            }
        }
        return list;
    }

    //获取日志中的所有对象检查器
    public static List<JavaStringObjectInspector> OICopy(Class<? extends AppBaseLog> clazz) {
        List<JavaStringObjectInspector> list = new ArrayList<JavaStringObjectInspector>();
        Class clazz1 = AppLogAggEntity.class;
        Field[] fields = clazz1.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                list.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            }
        }

        Field[] fields2 = clazz.getDeclaredFields();
        for (Field field2 : fields2) {
            field2.setAccessible(true);
            if (field2.getName() != "serialVersionUID" &&
                    field2.getName() != "country" &&
                    field2.getName() != "province" &&
                    field2.getName() != "ipAddress") {
                list.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            }
        }
        return list;
    }

}
