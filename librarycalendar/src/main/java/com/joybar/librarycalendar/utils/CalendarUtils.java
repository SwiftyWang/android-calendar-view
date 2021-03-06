package com.joybar.librarycalendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by joybar on 2/24/16.
 */
public class CalendarUtils {

    public static class CalendarSimpleDate {
        private int year;
        private int month;
        private int day;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public CalendarSimpleDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }


    /**
     * 获取上一月最后一天的日期数
     *
     * @param calendar
     * @return
     */
    public static int getDayOfLastMonth(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
//        System.out.println(calendar.get(Calendar.YEAR));
//        System.out.println(calendar.get(Calendar.MONTH)+1);
//        System.out.println(calendar.get(Calendar.DATE));
        int result = calendar.get(Calendar.DATE);
        calendar.add(Calendar.DATE, +1);//重新加一天，恢复
        return result;
    }

    /**
     * 获取上一月月份数
     *
     * @param calendar
     * @return
     */
    public static int getMonthOfLastMonth(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        int result = (calendar.get(Calendar.MONTH) + 1);
        calendar.add(Calendar.DATE, +1);//重新加一天，恢复
        return result;
    }


    /**
     * 获取上一月年份数
     *
     * @param calendar
     * @return
     */
    public static int getYearOfLastMonth(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
//        System.out.println(calendar.get(Calendar.YEAR));
//        System.out.println(calendar.get(Calendar.MONTH)+1);
//        System.out.println(calendar.get(Calendar.DATE));

        int result = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.DATE, +1);//重新加一天，恢复
        return result;
    }


    /**
     * 获取某年某月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDayCount(int year, int month) {
        Calendar cal = new GregorianCalendar(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据年月生成当月的日历中的日期
     *
     * @param year
     * @param month
     * @return
     * @throws ParseException
     */
    public static List<CalendarSimpleDate> getEverydayOfMonth(int year, int month) throws ParseException {
        List<CalendarSimpleDate> list = new ArrayList<>();
        int count = getDayCount(year, month); //获取当月的天数
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(year + "-" + month + "-" + 1));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int begin = cal.get(Calendar.DAY_OF_WEEK) - 1;//获取每月号星期几
        //重置
        cal.setTime(sdf.parse(year + "-" + month + "-" + 1));
        // int first = cal.get(Calendar.DAY_OF_WEEK);
        int dayOfLastMonth = getDayOfLastMonth(cal);
        int monthOfLastMonth = getMonthOfLastMonth(cal);
        int yearOfLastMonth = getYearOfLastMonth(cal);
        //填补上月的数据
        //每月第一天为星期日则不添加
        for (int i = 0; i < begin; i++) {
            CalendarSimpleDate calendarDate = new CalendarSimpleDate(yearOfLastMonth, monthOfLastMonth, dayOfLastMonth - begin + i + 1);
            list.add(calendarDate);
        }
        //填补本月的数据
        for (int i = 1; i <= count; i++) {
            CalendarSimpleDate calendarDate = new CalendarSimpleDate(year, month, i);
            list.add(calendarDate);
            cal.setTime(sdf.parse(year + "-" + month + "-" + i));

        }
        //填补下月的数据
        for (int i = 1; i < (7 - (begin - 1 + count) % 7); i++) {
            int tmpYear = year;
            int nextMonth = month + 1;
            if (nextMonth == 13) {
                nextMonth = 1;
                tmpYear = year + 1;
            }
            CalendarSimpleDate calendarDate = new CalendarSimpleDate(tmpYear, nextMonth, i);
            list.add(calendarDate);
        }
        return list;
    }

}
