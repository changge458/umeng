package com.it18zhang.logudtf;

import com.alibaba.fastjson.JSON;
import com.it18zhang.umeng.common.AppBaseLog;
import com.it18zhang.umeng.common.AppLogAggEntity;
import com.it18zhang.umeng.common.AppStartupLog;
import com.it18zhang.umeng.util.DateUtil;
import com.it18zhang.umeng.util.GeoliteUtil;
import com.it18zhang.umeng.util.LogUtil;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志聚合体解析表生成函数
 */
public abstract class LogForkBaseUDTF<T extends AppBaseLog> extends GenericUDTF{
	private Class<T> clazz ;
	//转换器集合
	private ObjectInspectorConverters.Converter[] converters ;

	//fieldNameList + oiList
	private Object[] arr;

	public LogForkBaseUDTF(){
		//获取泛型化超类
		ParameterizedType ptype = (ParameterizedType) this.getClass().getGenericSuperclass();
		//得到实际的类型参数,第一个
		clazz = (Class<T>) ptype.getActualTypeArguments()[0];
		//
		arr = getSIO(clazz);
	}

	/**
	 * 初始化方法
	 */
	public StructObjectInspector initialize(ObjectInspector[] ois) throws UDFArgumentException {
		//参数判定
		if(ois.length != 4){
			throw new UDFArgumentException("参数个数必须为4！");
		}
		//server_time
		if(!checkPrimitiveType(ois[0],PrimitiveObjectInspector.PrimitiveCategory.STRING)){
			throw new UDFArgumentException("server_time类型不是String！");
		}
		//remote ip
		if(!checkPrimitiveType(ois[1],PrimitiveObjectInspector.PrimitiveCategory.STRING)){
			throw new UDFArgumentException("remote_ip类型不是String！");
		}
		//client time
		if(!checkPrimitiveType(ois[2],PrimitiveObjectInspector.PrimitiveCategory.LONG)){
			throw new UDFArgumentException("client_time 类型不是bigInt！");
		}
		//json data
		if(!checkPrimitiveType(ois[3],PrimitiveObjectInspector.PrimitiveCategory.STRING)){
			throw new UDFArgumentException("json_data类型不是String！");
		}

		converters = new ObjectInspectorConverters.Converter[4] ;
		converters[0] = ObjectInspectorConverters.getConverter(ois[0] , PrimitiveObjectInspectorFactory.javaStringObjectInspector) ;
		converters[1] = ObjectInspectorConverters.getConverter(ois[1] , PrimitiveObjectInspectorFactory.javaStringObjectInspector) ;
		converters[2] = ObjectInspectorConverters.getConverter(ois[2] , PrimitiveObjectInspectorFactory.javaLongObjectInspector) ;
		converters[3] = ObjectInspectorConverters.getConverter(ois[3] , PrimitiveObjectInspectorFactory.javaStringObjectInspector) ;

		return ObjectInspectorFactory.getStandardStructObjectInspector((List<String>)arr[0] ,(List<ObjectInspector>)arr[1]) ;
	}

	/**
	 * 处理数据
	 */
	public void process(Object[] args) throws HiveException {
		//转换成json
		String json = (String) converters[3].convert(args[3]);
		json = json.replace("\\\"", "\"") ;

		//解析成log聚合体
		AppLogAggEntity aggLog = JSON.parseObject(json, AppLogAggEntity.class) ;

		//复制属性
		LogUtil.copyProperties2(aggLog,getAppLogList(aggLog));

		//时间对齐
		long client_time = (Long)converters[2].convert(args[2]) ;
		String server_time_str = (String) converters[0].convert(args[0]);
		long server_time = DateUtil.parseDateStringUS(server_time_str ,"dd/MMM/yyyy:HH:mm:ss Z") ;
		long offset = server_time - client_time ;
		LogUtil.alignTimeLogs(getAppLogList(aggLog),offset);

		//ip设置
		String ip = (String)converters[1].convert(args[1]);
		String country = GeoliteUtil.getCountry(ip);
		String province = GeoliteUtil.getProvince(ip);
		processIpForList(getAppLogList(aggLog),country,province);

		List<String> fieldNameList = (List<String>)arr[0];
		for(AppBaseLog log : getAppLogList(aggLog)){
			Object[] objArr = new Object[fieldNameList.size()] ;
			for(int i = 0 ; i < objArr.length ; i ++){
				objArr[i] = getLogValue(log,fieldNameList.get(i));
			}
			forward(objArr);
		}
	}

	/**
	 * 提取log对象属性为s的值
	 */
	public Object getLogValue(AppBaseLog log, String s) {
		try {
			BeanInfo bi = Introspector.getBeanInfo(log.getClass());
			PropertyDescriptor[] pps = bi.getPropertyDescriptors();
			for(PropertyDescriptor pp : pps ){
				if(pp.getName().equals(s)){
					Method m = pp.getReadMethod();
					return m.invoke(log) ;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}

	public void close() throws HiveException {
	}

	/**
	 * 检查基本类型
	 */
	public boolean checkPrimitiveType(ObjectInspector oi , PrimitiveObjectInspector.PrimitiveCategory cat){
		if(oi.getCategory() == ObjectInspector.Category.PRIMITIVE){
			if(((PrimitiveObjectInspector)oi).getPrimitiveCategory() == cat){
				return true ;
			}
		}
		return false ;
	}

	/**
	 * 得到指定类对应的结构体对象检查器
	 */
	public Object[] getSIO(Class clazz) {
		try {
			List<String> fieldNames = new ArrayList<String>() ;
			List<ObjectInspector> oiList = new ArrayList<ObjectInspector>();

			BeanInfo bi = Introspector.getBeanInfo(clazz) ;
			PropertyDescriptor[] pps = bi.getPropertyDescriptors();
			for(PropertyDescriptor pp : pps){
				Method getter = pp.getReadMethod();
				Method setter = pp.getWriteMethod();
				if(getter != null && setter != null){
					Class retType = getter.getReturnType();
					if(retType == String.class){
						fieldNames.add(pp.getName());
						oiList.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector) ;
					}
					else if(retType == Long.class || retType == long.class){
						fieldNames.add(pp.getName());
						oiList.add(PrimitiveObjectInspectorFactory.javaLongObjectInspector);
					}
					else if(retType == Integer.class || retType == int.class){
						fieldNames.add(pp.getName());
						oiList.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);
					}
				}
			}
			Object[] arr = new Object[2] ;
			arr[0] = fieldNames ;
			arr[1] = oiList ;
			return arr ;
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null ;
	}

	//获取要处理的日志子集
	public abstract List<T> getAppLogList(AppLogAggEntity aggLog)  ;

	/**
	 * 对集合进行ip处理
	 */
	public void processIpForList(List<T> logs, String country , String province){
	}
}
