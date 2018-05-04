package com.it18zhang.logudtf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义表生成函数
 */
public class MyUDTF extends GenericUDTF{

	private PrimitiveObjectInspector poi ;

	public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {
		//判断参数个数
		if(argOIs.length != 1){
			throw new UDFArgumentException("参数长度必须为1") ;
		}

		//判断参数类型
		ObjectInspector.Category cate = argOIs[0].getCategory() ;
		//
		if(cate == ObjectInspector.Category.PRIMITIVE){
			poi = (PrimitiveObjectInspector) argOIs[0];
			PrimitiveObjectInspector.PrimitiveCategory pc = poi.getPrimitiveCategory();
			if(pc != PrimitiveObjectInspector.PrimitiveCategory.STRING){
				throw new UDFArgumentException("参数必须是String");
			}
		}else{
			throw new UDFArgumentException("参数必须是String");
		}

		//字段名称集合
		List<String> fieldNames = new ArrayList<String>() ;
		fieldNames.add("col") ;

		//字符串对象检查器
		List<ObjectInspector> ois= new ArrayList<ObjectInspector>() ;
		ois.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector) ;

		return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames , ois ) ;
	}

	public void process(Object[] args) throws HiveException {
		Object o = args[0] ;

		//准备转换器对象
		ObjectInspectorConverters.Converter[] converters = new ObjectInspectorConverters.Converter[1];
		converters[0] = ObjectInspectorConverters.getConverter(poi , PrimitiveObjectInspectorFactory.javaStringObjectInspector) ;
		//进行转换
		String str = (String) converters[0].convert(o);
		//切割成数组
		String[] arr = str.split(",") ;
		for(String s : arr){
			Object[] oarr = new Object[1] ;
			oarr[0] = s ;
			forward(oarr);
		}
	}

	public void close() throws HiveException {
	}
}
