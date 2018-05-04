package com.it18zhang.umeng.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 *
 */
public class DateUtil {

    /**
     * 使用指定的locale和fmt进行解析串
     */
    public static long parseDateString(String dateStr ,String fmt, Locale locale){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt,locale) ;
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1 ;
    }

    /**
     * 使用指定的locale和fmt进行解析串
     */
    public static long parseDateStringLocal(String dateStr ,String fmt){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt,Locale.CHINA) ;
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1 ;
    }

    /**
     * 使用指定的locale和fmt进行解析串
     */
    public static long parseDateStringUS(String dateStr ,String fmt){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt,Locale.US) ;
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1 ;
    }

    //格式化时间戳成yyyyMMdd
    public static String formatTime(long ms , String fmt){
        Date date = new java.util.Date(ms);
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date) ;
    }


    /**
     * 得到指定日期的零时刻
     * @param d
     * @param offset
     * @return
     */

    public static long getDayBegin(Date d , int offset){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DAY_OF_MONTH,offset);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            String zeroStr = sdf1.format(c.getTime());

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date zeroDate = sdf2.parse(zeroStr);

            long timestamp = zeroDate.getTime();
            return timestamp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到指定日期的零时刻
     * @param dateStr
     * @param fmt
     * @param offset
     * @return
     */

    public static long getDayBegin(String dateStr, String fmt, int offset){
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fmt);

            Date date = sdf.parse(dateStr);

            return getDayBegin(date,offset);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到指定所在周的零时刻
     * @param d         指定日期
     * @param offset    周偏移量
     * @return
     */
    public static long getWeekBegin(Date d , int offset){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(d);

            //得到该时间位于一周中的第几天
            int which = c.get(Calendar.DAY_OF_WEEK);

            c.add(Calendar.DAY_OF_MONTH, -(which - 1));
            c.add(Calendar.DAY_OF_MONTH, offset * 7);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            String zeroStr = sdf1.format(c.getTime());

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date zeroDate = sdf2.parse(zeroStr);

            return zeroDate.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getWeekBegin(String dateStr, String fmt, int offset){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            Date d = sdf.parse(dateStr);
            return getWeekBegin(d,offset);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 得到指定所在月份的零时刻
     */
    public static long getMonthBegin(Date d , int offset){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.MONTH,offset);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            String zeroStr = sdf1.format(c.getTime());

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date zeroDate = sdf2.parse(zeroStr);

            return zeroDate.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 得到指定所在月份的零时刻
     * @param dateStr
     * @param fmt
     * @param offset
     * @return
     */
    public static long getMonthBegin(String dateStr, String fmt, int offset){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            Date d = sdf.parse(dateStr);
            return getMonthBegin(d,offset);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



}
