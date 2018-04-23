package com.joybar.librarycalendar.controller;


import android.util.LruCache;

import com.joybar.librarycalendar.data.CalendarDate;
import com.joybar.librarycalendar.data.Lunar;
import com.joybar.librarycalendar.data.Solar;
import com.joybar.librarycalendar.utils.CalendarUtils;
import com.joybar.librarycalendar.utils.LunarSolarConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * Created by joybar on 2/24/16.
 */
public class CalendarDateController {

    public static LruCache<Integer, List<CalendarDate>> cachedDate = new LruCache<>(24);

    public static List<CalendarDate> getCalendarDate(int year, int month, HashSet<String> selectedDates) {
        int cacheKey = year * 100 + month;
        if (cachedDate.get(cacheKey) != null) {
            return cachedDate.get(cacheKey);
        }
        List<CalendarDate> mListDate = new ArrayList<>();
        List<CalendarUtils.CalendarSimpleDate> list = null;
        try {
            list = CalendarUtils.getEverydayOfMonth(year, month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int count = list.size();

        for (int i = 0; i < count; i++) {
            Solar solar = new Solar();
            solar.solarYear = list.get(i).getYear();
            solar.solarMonth = list.get(i).getMonth();
            solar.solarDay = list.get(i).getDay();
            String dateString = solar.getDateString();
            boolean isSelected = selectedDates.contains(dateString);
            Lunar lunar = LunarSolarConverter.SolarToLunar(solar);
            mListDate.add(new CalendarDate(month == list.get(i).getMonth(), isSelected, solar, lunar));
        }
        cachedDate.put(cacheKey, mListDate);
        return mListDate;
    }


}
