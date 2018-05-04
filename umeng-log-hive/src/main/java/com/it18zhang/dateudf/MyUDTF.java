package com.it18zhang.dateudf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义表生成函数
 */
public class MyUDTF extends GenericUDTF {

    private PrimitiveObjectInspector inputOI = null;

    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {


        //
        if (argOIs.getAllStructFieldRefs().size() != 1) {
            System.out.println("need only one param");
            System.exit(0);
        }

        //
        ObjectInspector oi = argOIs.getAllStructFieldRefs().get(0).getFieldObjectInspector();

        if (oi.getCategory() != ObjectInspector.Category.PRIMITIVE) {
            System.out.println("need primitive type");
            System.exit(0);
        }

        //
        inputOI = (PrimitiveObjectInspector) oi;

        if (inputOI.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
            System.out.println("need string");
            System.exit(0);
        }

        //
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("col");

        List<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    /**
     * 一行调用一次此方法，使用forward来输出
     */
    @Override
    public void process(Object[] record) throws HiveException {
        /*
         * 将object类型强转为String
         */

        final String recStr = (String) inputOI.getPrimitiveJavaObject(record[0]);
        // 输出返回的定义结构String
        String[] arr = recStr.split(" ");
        for (String word : arr) {
            forward(new Object[]{word});
        }
    }

    @Override
    public void close() throws HiveException {

    }
}