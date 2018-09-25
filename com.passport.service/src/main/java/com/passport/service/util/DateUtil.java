package com.passport.service.util;

import com.passport.rpc.dto.DateType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/19 17:41
 */
public class DateUtil {

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

    public static Date[] getStartAndEndDate(DateType type) throws Exception{
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = null;
        String str = null;
        Date startTime = null;
        Date nowDate = new Date();
        cal.setTime(nowDate);
        switch (type){
            case ALL:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                str = "2001-01-01 00:00:00";
                startTime = sdf.parse(str);
                break;
            case DAY:
                startTime = setZero(cal);
                break;
            case WEEK:
                int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
                if (dayofweek == 1) {
                    dayofweek += 7;
                }
                cal.add(Calendar.DATE, 2 - dayofweek);
                startTime = setZero(cal);
                break;
            case MONTH:
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
                startTime = cal.getTime();
                break;
            case YEAY:
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.YEAR));
                startTime = cal.getTime();
                break;
            default:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                str = "2001-01-01 00:00:00";
                startTime = sdf.parse(str);
        }
        return new Date[]{startTime,nowDate};
    }
}
