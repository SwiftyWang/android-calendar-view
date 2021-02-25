package com.joybar.librarycalendar.adapter;

import android.util.Log;

import com.joybar.librarycalendar.fragment.CalendarViewFragment;
import com.joybar.librarycalendar.utils.DateUtils;

import java.util.HashSet;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


/**
 * Created by joybar on 4/27/16.
 */
public class CalendarViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = CalendarViewPagerAdapter.class.getSimpleName();
    private boolean mIsSaturdayHoliday;
    private int number;
    private int NUM_ITEMS = 200;
    private int NUM_ITEMS_CURRENT = NUM_ITEMS / 2;
    private boolean isChoiceModelSingle;
    private HashSet<String> mSelectedDate;

    public CalendarViewPagerAdapter(FragmentManager fm, boolean isChoiceModelSingle, int startYear, int endYear, @Nullable HashSet<String> selectedDate, boolean isSaturdayHoliday) {
        super(fm);
        this.isChoiceModelSingle = isChoiceModelSingle;
        int thisMonthPosition = DateUtils.getYear() * 12 + DateUtils.getMonth() - 1;
        if (startYear > 1970 && endYear >= startYear) {
            NUM_ITEMS = (endYear - startYear + 1) * 12;
            int offset = thisMonthPosition - startYear * 12;
            if (offset > 0 && offset < 12) {
                NUM_ITEMS_CURRENT = offset;
                number = thisMonthPosition - NUM_ITEMS_CURRENT;
            } else {
                NUM_ITEMS_CURRENT = 0;
                number = startYear * 12;
            }
        } else {
            number = thisMonthPosition - NUM_ITEMS_CURRENT;
        }
        mSelectedDate = selectedDate;
        setIsSaturdayHoliday(isSaturdayHoliday);
    }

    public int getDefaultPosition() {
        return NUM_ITEMS_CURRENT;
    }

    @Override
    public Fragment getItem(int position) {
        int year = getYearByPosition(position);
        int month = getMonthByPosition(position);
        return CalendarViewFragment.newInstance(year, month, isChoiceModelSingle, parseCurrentMonthSelectedDate(year, month), mIsSaturdayHoliday);
    }

    public void setSelectedDate(HashSet<String> selectedDate) {
        mSelectedDate = selectedDate;
    }

    private HashSet<String> parseCurrentMonthSelectedDate(int year, int month) {
        if (mSelectedDate == null) {
            return null;
        }
        HashSet<String> currentMonthSelectedDate = new HashSet<>();
        for (String date : mSelectedDate) {
            String[] split = date.split("/");
            try {
                if (split.length == 3 && Integer.parseInt(split[0]) == year && Integer.parseInt(split[1]) == month) {
                    currentMonthSelectedDate.add(date);
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, e.toString(), e);
            }
        }
        return currentMonthSelectedDate;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    public int getYearByPosition(int position) {
        int year = (position + number) / 12;
        return year;
    }

    public int getMonthByPosition(int position) {
        int month = (position + number) % 12 + 1;
        return month;
    }

    public void setIsSaturdayHoliday(boolean isSaturdayHoliday) {
        mIsSaturdayHoliday = isSaturdayHoliday;
    }
}
