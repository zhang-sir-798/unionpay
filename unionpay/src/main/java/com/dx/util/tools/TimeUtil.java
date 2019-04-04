package com.dx.util.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName TimeUtil
 * @Description 时间工具类
 * @author 姑娘、请留步
 * @Date 2018年12月12日 下午6:38:25
 * @version 1.0.0
 * 
 */
public class TimeUtil {

	public static final String TIME_FORMAT = "yyyyMMddHHmmss";

	public static final int TIME_EXPIRE = 2;

	
	public static String getStartTime(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return sdf.format(date);
	}
	//缺省值：2小时
	public static String getExpireTime(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return sdf.format(addHour(date, TIME_EXPIRE));
	}

	public static Date addHour(Date date, int time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, time);
		return cal.getTime();
	}

}
