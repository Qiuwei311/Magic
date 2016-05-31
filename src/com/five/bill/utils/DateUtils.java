package com.five.bill.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类 默认使用 "yyyy-MM-dd HH:mm:ss" 格式化日期
 * 
 */
@SuppressLint("SimpleDateFormat") 
public final class DateUtils {
	/**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";
    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
    public static String FORMAT_FULL_2 = "yyyyMMddHHmmss";
    /**
     * 中文简写 如：2010年12月01日
     */
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd";
    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
	
	public static SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
	public static Calendar mCalendar = Calendar.getInstance();

	/**
	 * 
	 * @param dateString
	 * @param formatStr
	 * @return
	 */
	public static String FormatDateStrng(String dateString) {
		if (dateString == null || dateString.equals("null")) {
			return "";
		}
		if (dateString.length() > 0) {
			dateString = dateString.substring(0, 10);
		}
		return dateString;
	}
		
	public static String getToday() {  
		Date curDate = new Date(System.currentTimeMillis());   
		return mFormatter.format(curDate);   
	}
	
	public static String getCurrentDate() {		
		Calendar c = Calendar.getInstance();
		return new SimpleDateFormat("yyyy-MM").format(c.getTime());
	}
	
	public static String getNextMonth(String current) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(stringToDate("yyyy-MM", current));
		calendar.set(2, calendar.get(Calendar.MONTH) + 1);
		return new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
	}
	
	public static String getFirstDayOfMonth(String month) {
		mCalendar.setTime(stringToDate("yyyy-MM", month));
		mCalendar.add(Calendar.MONTH, 0);
		mCalendar.set(Calendar.DAY_OF_MONTH, 1);
		return mFormatter.format(mCalendar.getTime());
	}
	
	public static String getLastDayOfMonth(String month) {
		mCalendar.setTime(stringToDate("yyyy-MM", month));
		mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return mFormatter.format(mCalendar.getTime());
	}
	
	public static Date stringToDate(String style, String date) {
		if (date == null || date.equals("")) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public static String dateToString(String style, Date date) {
		if ((date == null) || (date.equals(""))) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(date);
	}
	
	
	public static int calculateDuration(Date startTime, Date endTime) {
		int duration = (int)(1 + (endTime.getTime() - startTime.getTime())
				/ (24 * 3600 * 1000));
		return duration;
	}
	
	public static String parseMonthSection(Date date) {
        if ((date == null) || (date.equals(""))) {
            return "";
        }
        
        if (getCurrentDate().equals(dateToString("yyyy-MM", date))) {
            return "本月";
        }
        
	    String[] sections = {"本月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
	    SimpleDateFormat formatter = new SimpleDateFormat("MM");
	    String month = formatter.format(date);
	    return sections[Integer.valueOf(month)];
	}

}
