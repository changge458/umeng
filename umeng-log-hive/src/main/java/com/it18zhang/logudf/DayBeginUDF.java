package com.it18zhang.logudf;

import com.it18zhang.umeng.util.DateUtil;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Date;

/**
 * 计算某天的零时刻对应的毫秒数
 */
public class DayBeginUDF extends UDF{

    public long evaluate(){
        return DateUtil.getDayBegin(new Date(),0);
    }
    public long evaluate(int offset){
        return DateUtil.getDayBegin(new Date(), offset);
    }

    public long evaluate(String dateStr ,String fmt , int offset){
        return DateUtil.getDayBegin(dateStr,fmt , offset);
    }

}