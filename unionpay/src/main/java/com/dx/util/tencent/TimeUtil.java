package com.dx.util.tencent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hyman on 2017/2/28.
 */
public class TimeUtil {

    //TODO 都未处理输入参数

    public static String getFormatTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    //两天
    public static Date addDay(Date date, int time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, time);
        return cal.getTime();
    }

    public static Date minusDay(Date date, int day) {
        return addDay(date, -day);
    }

    

}
