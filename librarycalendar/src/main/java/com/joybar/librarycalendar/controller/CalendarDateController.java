package com.joybar.librarycalendar.controller;


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

    public static List<CalendarDate> getCalendarDate(int year, int month, HashSet<String> selectedDates) {

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

        return mListDate;
    }


}
