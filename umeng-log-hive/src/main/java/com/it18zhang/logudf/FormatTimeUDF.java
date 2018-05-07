package com.it18zhang.logudf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式化时间函数
 */
public class FormatTimeUDF extends UDF {
	//
	public String evaluate(long time , String fmt) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt) ;
			Date d = new Date(time) ;
			return sdf.format(d) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "" ;
	}
}
