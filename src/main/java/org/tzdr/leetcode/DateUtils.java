package org.tzdr.leetcode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 狐妖小红娘
 * @version 2018年12月19日 上午8:25:55
 * 	日期操作练习	默认的格式化模式	yyyy-MM-dd HH:mm:ss
 */
public class DateUtils {
	/**
	 * 	一些碎碎念！java.util.Date 类重写toString方法   里面初始化StringBuilder对象的时候指定了容量，28个字符
	 * 	日期的格式化也可以自己使用Calendar，来进行实现.获得一个Date对象的年、月、日：时、分、秒  来进行拼接格式化
	 * 	获得日期的年、月、日，可以格式化之后，使用substring方法来实现
	 */
	
	/**
	 * 	时间戳模式	精确到毫秒的完整时间
	 */
	public static final String timestamp_pattern="yyyy-MM-dd HH:mm:ss.S";
	
	/**
	 * 	年月日  时分秒 模式
	 */
	public static final String date_time_pattern="yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 	只有日期的模式
	 */
	public static final String date_pattern="yyyy-MM-dd";
	
	/**
	 * 	只有时间的模式
	 */
	public static final String time_pattern="HH:mm:ss";
	
    /**
     * 	获得默认的日期格式化模式	yyyy-MM-dd HH:mm:ss
     */
    public static String getDatePattern() {
        return date_time_pattern;
    }
	
	/**
	 * 	根据默认模式 yyyy-MM-dd HH:mm:ss  返回当前时间
	 * @return
	 */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 	返回指定模式的 当前时间
     * @param format
     * @return
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }

    /**
     * 	使用默认模式格式化时间
     * @param date
     * 			日期
     * @return
     */
    public static String format(Date date) {
        return format(date, getDatePattern());
    }
	
    /**
     * 	使用指定的模式来格式化日期
     * @param date	
     * 			日期
     * @param pattern
     * 			格式化日期的模式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }
    
    /**
     * 	使用默认的格式，获取字符串中的日期
     * @param strDate
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }
	
    /**
     * 	使用指定的模式，获取字符串总的日期
     * @param strDate
     * @param pattern
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
	
    /**
     * 	在指定日期增加月数
     * @param date
     * @param n
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }
    
    /**
     * 	在指定日期增加天数
     * @param date
     * @param n
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }
    
    /**
     * 	获得时间戳
     * @return
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(timestamp_pattern);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }
    
    public static void main(String[] args) {
    	String format = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS";
         
		System.out.println(String.format(format, new Date()));
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	
}
