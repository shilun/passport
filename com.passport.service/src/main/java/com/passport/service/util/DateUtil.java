package com.passport.service.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/19 17:41
 */
public class DateUtil {

    public static Date getTomorrowZeroTime(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,1);
        return setZero(cal);
    }

    /**
     * 将时分秒置为0
     * @param date
     * @return
     */
    public static Date setZero(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return setZero(cal);
    }
    /**
     * 将时分秒置为0
     * @param cal
     * @return
     */
    public static Date setZero(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }
    /**
     * 获取两个日期之间的相隔天数
     * @param before
     * @param after
     * @return
     */
    public static int differentDays(Date before, Date after){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(before);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(after);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2){
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++){
                if(i%4==0 && i%100!=0 || i%400==0){
                    timeDistance += 366;
                }else{
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }else{
            return day2-day1;
        }
    }
}
