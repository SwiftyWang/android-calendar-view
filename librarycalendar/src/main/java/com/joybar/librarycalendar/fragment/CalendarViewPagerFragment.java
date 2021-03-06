package com.joybar.librarycalendar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joybar.librarycalendar.R;
import com.joybar.librarycalendar.adapter.CalendarViewPagerAdapter;
import com.joybar.librarycalendar.controller.CalendarDateController;
import com.joybar.librarycalendar.data.CalendarDate;

import java.util.HashSet;


/**
 * Created by joybar on 4/28/16.
 */
public class CalendarViewPagerFragment extends Fragment {

    public static final String ARG_SELECTED_DATE = "ARG_SELECTED_DATE";
    private static final String ARG_START_YEAR = "ARG_START_YEAR";
    private static final String ARG_END_YEAR = "ARG_END_YEAR";
    private static final String CHOICE_MODE_SINGLE = "choice_mode_single";
    private static final String ARG_IS_SATURDAY_HOLIDAY = "ARG_IS_SATURDAY_HOLIDAY";
    private int endYear;
    private boolean isChoiceModelSingle;
    private CalendarViewPagerAdapter myAdapter;
    private HashSet<String> selectedDate;
    private int startYear;
    private ViewPager viewPager;
    private OnPageChangeListener onPageChangeListener;
    private boolean isSaturdayHoliday;

    public CalendarViewPagerFragment() {
    }

    public static CalendarViewPagerFragment newInstance(boolean isChoiceModelSingle, HashSet<String> selectedYearMonthDayList, int startYear, int endYear, boolean isSaturdayHoliday) {
        CalendarViewPagerFragment fragment = new CalendarViewPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SELECTED_DATE, selectedYearMonthDayList);
        args.putInt(ARG_START_YEAR, startYear);
        args.putInt(ARG_END_YEAR, endYear);
        args.putBoolean(CHOICE_MODE_SINGLE, isChoiceModelSingle);
        args.putBoolean(ARG_IS_SATURDAY_HOLIDAY, isSaturdayHoliday);
        fragment.setArguments(args);
        return fragment;
    }

    public void addSelectedDate(CalendarDate calendarDate) {
        selectedDate.add(calendarDate.getSolarDateString());
        if (myAdapter != null) {
            myAdapter.setSelectedDate(selectedDate);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onPageChangeListener = (OnPageChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnDateClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isChoiceModelSingle = getArguments().getBoolean(CHOICE_MODE_SINGLE, false);
            startYear = getArguments().getInt(ARG_START_YEAR, 0);
            endYear = getArguments().getInt(ARG_END_YEAR, 0);
            selectedDate = (HashSet<String>) getArguments().getSerializable(ARG_SELECTED_DATE);
            isSaturdayHoliday = getArguments().getBoolean(ARG_IS_SATURDAY_HOLIDAY);
            if (selectedDate == null) {
                selectedDate = new HashSet<>();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_viewpager, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        initViewPager();
        return view;
    }

    private void initViewPager() {
        myAdapter = new CalendarViewPagerAdapter(getChildFragmentManager(), isChoiceModelSingle, startYear, endYear, selectedDate, isSaturdayHoliday);
        viewPager.setAdapter(myAdapter);
        viewPager.setCurrentItem(myAdapter.getDefaultPosition());
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int year = myAdapter.getYearByPosition(position);
                int month = myAdapter.getMonthByPosition(position);
                CalendarViewPagerFragment.this.onPageChangeListener.onPageChange(year, month);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        viewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(myAdapter.getDefaultPosition());
    }

    public void removeSelectedDate(CalendarDate calendarDate) {
        selectedDate.remove(calendarDate.getSolarDateString());
        if (myAdapter != null) {
            myAdapter.setSelectedDate(selectedDate);
        }
    }

    public void setSaturdayIsHoliday(boolean isHoliday) {
        isSaturdayHoliday = isHoliday;
        updateViewPager();
    }

    private void updateViewPager() {
        int currentItem = viewPager.getCurrentItem();
        myAdapter = new CalendarViewPagerAdapter(getChildFragmentManager(), isChoiceModelSingle, startYear, endYear, selectedDate, isSaturdayHoliday);
        viewPager.setAdapter(myAdapter);
        viewPager.setCurrentItem(currentItem);
    }

    public void removeAllSelectedDate() {
        selectedDate = new HashSet<>();
        CalendarDateController.cachedDate.evictAll();
        updateViewPager();
    }

    public interface OnPageChangeListener {
        void onPageChange(int year, int month);
    }
}
