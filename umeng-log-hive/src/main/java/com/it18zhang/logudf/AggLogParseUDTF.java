package com.it18zhang.logudf;

import com.it18zhang.umeng.common.AppLogAggEntity;
import com.it18zhang.umeng.util.GeoliteUtil;
import com.it18zhang.umeng.util.ParseLogUtil;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.JavaStringObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志聚合体解析表生成函数
 */
@Description(name = "parseAgg",
        value = "select parseAgg(line) ===> server_time, remote_ip, client_time, appid, tenantid, deviceid, appversion, appchannel, appplatform, ostype, devicestyle\t" +
                "select parseAgg(line,1) ===> server_time, remote_ip, client_time, appid, tenantid, deviceid, appversion, appchannel, appplatform, ostype, devicestyle, json\t",
        extended = "the 2nd can be not exist. if it exists, it must be one of 1,2,3,4,5")

public class AggLogParseUDTF extends GenericUDTF {

    private PrimitiveObjectInspector line_inputOI = null;
    private PrimitiveObjectInspector type_inputOI = null;

    private static final int LOG_STARTUP = 1;
    private static final int LOG_ERROR = 2;
    private static final int LOG_EVENT = 3;
    private static final int LOG_PAGE = 4;
    private static final int LOG_USAGE = 5;

    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {


        //检查参数个数
        if (argOIs.getAllStructFieldRefs().size() == 1 ) {
            //取得日志的ObjectInspector对象
            ObjectInspector line_oi = argOIs.getAllStructFieldRefs().get(0).getFieldObjectInspector();


            //
            line_inputOI = (PrimitiveObjectInspector) line_oi;

            if (line_inputOI.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
                throw new UDFArgumentException("参数1必须是string类型！");
            }

            //
            List<String> fieldNames = new ArrayList<String>();

            fieldNames.add("server_time");
            fieldNames.add("remote_ip");
            fieldNames.add("country");
            fieldNames.add("province");

            fieldNames.add("client_time");
            fieldNames.addAll(fieldsCopy());


            List<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.addAll(OICopy());

            return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);

        }
        if( argOIs.getAllStructFieldRefs().size() ==2){
            //取得日志的ObjectInspector对象
            ObjectInspector line_oi = argOIs.getAllStructFieldRefs().get(0).getFieldObjectInspector();
            ObjectInspector type_oi = argOIs.getAllStructFieldRefs().get(1).getFieldObjectInspector();


            //
            line_inputOI = (PrimitiveObjectInspector) line_oi;
            type_inputOI = (PrimitiveObjectInspector) type_oi;

            if (line_inputOI.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
                throw new UDFArgumentException("参数1必须是string类型！");
            }
            if (type_inputOI.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.INT) {
                throw new UDFArgumentException("参数2必须是int类型！ < 1:启动日志, 2:错误日志, 3:事件日志, 4:页面日志, 5:使用日志 >");
            }

            //
            List<String> fieldNames = new ArrayList<String>();
            fieldNames.add("server_time");
            fieldNames.add("remote_ip");
            fieldNames.add("country");
            fieldNames.add("province");

            fieldNames.add("client_time");
            fieldNames.addAll(fieldsCopy());
            fieldNames.add("json");

            List<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.addAll(OICopy());
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

            return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);

        }
        else{
            throw new UDFArgumentException("参数个数必须为1或2！");
        }
    }

    /**
     * 一行调用一次此方法，使用forward来输出
     */
    @Override
    public void process(Object[] record) throws HiveException {
        if(record.length == 1){
            /*
             * 将第一个参数line转换成String类型
             */
            final String line = (String) line_inputOI.getPrimitiveJavaObject(record[0]);

            // 将line进行截串，获取到所需的参数
            String[] arr = line.split("#");
            Object[] obj = new Object[13];
            //server_time
            obj[0] = arr[0];
            //remote_ip
            obj[1] = arr[1];

            //country
            obj[2] = GeoliteUtil.getCountry(arr[1]);

            //province
            obj[3] = GeoliteUtil.getProvince(arr[1]);

            //client_time
            obj[4] = arr[2];
            //

            List<String> fields = fieldsCopy();
            for(int i = 5; i < 13; i++){
                obj[i] = ParseLogUtil.parseJson(fields.get(i - 5),arr[4]);
            }
            forward(obj);
        }

        if(record.length == 2){
            /*
             * 将第一个参数line转换成String类型
             */
            final String line = (String) line_inputOI.getPrimitiveJavaObject(record[0]);
            /*
             * 将第二个参数type转换成int类型
             */
            final Integer type = (Integer) type_inputOI.getPrimitiveJavaObject(record[1]);

            // 将line进行截串，获取到所需的参数
            String[] arr = line.split("#");
            Object[] obj = new Object[14];
            //server_time
            obj[0] = arr[0];
            //remote_ip
            obj[1] = arr[1];

            //country
            obj[2] = GeoliteUtil.getCountry(arr[1]);

            //province
            obj[3] = GeoliteUtil.getProvince(arr[1]);

            //client_time
            obj[4] = arr[2];

            List<String> fields = fieldsCopy();
            for(int i = 5; i < 13; i++){
                obj[i] = ParseLogUtil.parseJson(fields.get(i - 5),arr[4]);
            }
            //json
            switch (type) {
                case LOG_STARTUP:
                    obj[13] = ParseLogUtil.parseStartup(arr[4]);
                    break;
                case LOG_ERROR:
                    obj[13] = ParseLogUtil.parseError(arr[4]);
                    break;
                case LOG_EVENT:
                    obj[13] = ParseLogUtil.parseEvent(arr[4]);
                    break;
                case LOG_PAGE:
                    obj[13] = ParseLogUtil.parsePage(arr[4]);
                    break;
                case LOG_USAGE:
                    obj[13] = ParseLogUtil.parseUsage(arr[4]);
                    break;
                default :
                    throw new UDFArgumentException("参数2必须是以下数字！ < 1:启动日志, 2:错误日志, 3:事件日志, 4:页面日志, 5:使用日志 >");

            }
            forward(obj);
        }

    }

    @Override
    public void close() throws HiveException {

    }

    //获取logAgg中的所有字段
    public static List<String> fieldsCopy(){
        List<String> list = new ArrayList<String>();
        Class clazz = AppLogAggEntity.class;
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            if(field.getType() == String.class){
                list.add(field.getName());
            }
        }
        return list;
    }
    //获取logAgg中的所有对象检查器
    public static List<JavaStringObjectInspector> OICopy(){
        List<JavaStringObjectInspector> list = new ArrayList<JavaStringObjectInspector>();
        Class clazz = AppLogAggEntity.class;
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            if(field.getType() == String.class){
                list.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            }
        }
        return list;
    }


}