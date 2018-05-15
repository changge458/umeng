package com.it18zhang.logudf;

import com.it18zhang.umeng.common.AppEventLog;
import com.it18zhang.umeng.common.AppUsageLog;
import com.it18zhang.umeng.util.GeoliteUtil;
import com.it18zhang.umeng.util.ParseLogUtil;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.JavaStringObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.List;

import static com.it18zhang.umeng.util.LogUtil.OICopy;
import static com.it18zhang.umeng.util.LogUtil.fieldsCopy;

/**
 * 日志聚合体解析表生成函数
 */
@Description(name = "parseUsage",
        value = "select parseUsage(line) ===> server_time, remote_ip, client_time, tenantid, deviceid, appversion, appchannel, appplatform, ostype, devicestyle, singleUseDurationSecs, singleUploadTraffic, singleDownloadTraffic\t")

public class ParseUsageUDTF extends GenericUDTF {

    private PrimitiveObjectInspector line_inputOI = null;
    private List<String> fields = fieldsCopy(AppUsageLog.class);
    private List<JavaStringObjectInspector> ois = OICopy(AppUsageLog.class);


    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {


        //检查参数个数
        if (argOIs.getAllStructFieldRefs().size() == 1) {
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
            fieldNames.add("createAtMs");
            fieldNames.addAll(fields);


            List<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
            fieldOIs.addAll(ois);


            return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);

        }
       else {
            throw new UDFArgumentException("参数个数必须为1！");
        }
    }

    /**
     * 一行调用一次此方法，使用forward来输出
     */
    @Override
    public void process(Object[] record) throws HiveException {
        /*
         * 将第一个参数line转换成String类型
         */
        final String line = (String) line_inputOI.getPrimitiveJavaObject(record[0]);

        //获取log数组
        String[] arr = line.split("#");
        String usageJson = ParseLogUtil.parseUsage(arr[4]);

        //获取整个日志数组
        List<String> list = ParseLogUtil.parseJsonToArray("appUsageLogs",usageJson);

        int size = fields.size();

        for(String json: list){
            // 将line进行截串，获取到所需的参数
            Object[] obj = new Object[size+6];
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

            //createAtMs
            obj[5] = ParseLogUtil.parseJson("createdAtMs", json);

            //obj[4] ---> obj[11] : LogAgg
            for(int i = 0; i < 8; i++){
                obj[i+6] = ParseLogUtil.parseJson(fields.get(i),arr[4]);
            }

            //obj[12] ---> obj[size] : LogError
            for(int j = 8; j < size ; j++){
                obj[j+6] = ParseLogUtil.parseJson(fields.get((j)), json);
            }
            forward(obj);
        }
    }

    @Override
    public void close() throws HiveException {

    }

}