package com.joybar.librarycalendar.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.joybar.librarycalendar.R;
import com.joybar.librarycalendar.adapter.CalendarGridViewAdapter;
import com.joybar.librarycalendar.controller.CalendarDateController;
import com.joybar.librarycalendar.data.CalendarDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.fragment.app.Fragment;


/**
 * Created by joybar on 4/27/16.
 */
public class CalendarViewFragment extends Fragment {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String CHOICE_MODE_SINGLE = "choice_mode_single";
    private static final String ARG_IS_SATURDAY_HOLIDAY = "ARG_IS_SATURDAY_HOLIDAY";
    CalendarGridViewAdapter calendarGridViewAdapter;
    private boolean isChoiceModelSingle;
    private View mProgress;
    private int mYear;
    private int mMonth;
    private GridView mGridView;
    private OnDateClickListener onDateClickListener;
    private OnDateCancelListener onDateCancelListener;
    private HashSet<String> selectedDates;
    private ProcessTask mProcessTask;
    private boolean isSaturdayHoliday;
    private List<CalendarDate> mCalendarDateList = new ArrayList<>();
    private final ProcessTask.DateLoadListener mDateLoadListener = new ProcessTask.DateLoadListener() {

        @Override
        public void onDateLoaded(final List<CalendarDate> calendarDateList) {
            mProgress.setVisibility(View.GONE);
            mCalendarDateList = calendarDateList;
            updateList();
            mGridView.post(new Runnable() {
                @Override
                public void run() {
                    //需要默认选中当天
                    List<CalendarDate> mListData = ((CalendarGridViewAdapter) mGridView.getAdapter()).getListData();
                    int count = mListData.size();
                    for (int i = 0; i < count; i++) {
                        if (mListData.get(i).getSelectStatus() == CalendarDate.SelectStatus.SelectWhole) {
                            mGridView.setItemChecked(i, true);
                        }
                    }

                }
            });
        }
    };

    public CalendarViewFragment() {
    }

    //todo change to builder pattern
    public static CalendarViewFragment newInstance(int year, int month, boolean isChoiceModelSingle, HashSet<String> selectedDates, boolean mIsSaturdayHoliday) {
        CalendarViewFragment fragment = new CalendarViewFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        args.putBoolean(CHOICE_MODE_SINGLE, isChoiceModelSingle);
        args.putBoolean(ARG_IS_SATURDAY_HOLIDAY, mIsSaturdayHoliday);
        args.putSerializable(CalendarViewPagerFragment.ARG_SELECTED_DATE, selectedDates);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onDateClickListener = (OnDateClickListener) context;
            if (!isChoiceModelSingle) {
                //多选
                onDateCancelListener = (OnDateCancelListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnDateClickListener or OnDateCancelListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mYear = getArguments().getInt(YEAR);
            mMonth = getArguments().getInt(MONTH);
            isChoiceModelSingle = getArguments().getBoolean(CHOICE_MODE_SINGLE, false);
            isSaturdayHoliday = getArguments().getBoolean(ARG_IS_SATURDAY_HOLIDAY, false);
            selectedDates = (HashSet<String>) getArguments().getSerializable(CalendarViewPagerFragment.ARG_SELECTED_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mGridView = (GridView) view.findViewById(R.id.gv_calendar);
        mProgress = view.findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);
        mProcessTask = new ProcessTask(mYear, mMonth, selectedDates);
        mProcessTask.setDateLoadListener(mDateLoadListener);
        mProcessTask.execute();

        calendarGridViewAdapter = new CalendarGridViewAdapter(mCalendarDateList, isSaturdayHoliday);
        mGridView.setAdapter(calendarGridViewAdapter);
        if (isChoiceModelSingle) {
            mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        } else {
            mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalendarDate calendarDate = ((CalendarGridViewAdapter) mGridView.getAdapter()).getListData().get(position);
                if (isChoiceModelSingle) {
                    //单选
                    if (mCalendarDateList.get(position).isInThisMonth()) {
                        addSelectedDate(calendarDate);
                        mCalendarDateList.get(position).nextSelectStatus();
                        onDateClickListener.onDateClick(calendarDate);
                    } else {
                        mGridView.setItemChecked(position, false);
                    }
                } else {
                    //多选
                    if (mCalendarDateList.get(position).isInThisMonth()) {
                        // mGridView.getCheckedItemIds()
                        if (!mGridView.isItemChecked(position)) {
                            removeSelectedDate(calendarDate);
                            mCalendarDateList.get(position).setSelectStatus(CalendarDate.SelectStatus.NotSelect);
                            onDateCancelListener.onDateCancel(calendarDate);
                        } else {
                            addSelectedDate(calendarDate);
                            mCalendarDateList.get(position).setSelectStatus(CalendarDate.SelectStatus.SelectWhole);
                            onDateClickListener.onDateClick(calendarDate);
                        }

                    } else {
                        mGridView.setItemChecked(position, false);
                    }

                }
            }
        });
        return view;
    }

    private void removeSelectedDate(CalendarDate calendarDate) {
        if (getParentFragment() instanceof CalendarViewPagerFragment) {
            ((CalendarViewPagerFragment) getParentFragment()).removeSelectedDate(calendarDate);
        }
    }

    private void addSelectedDate(CalendarDate calendarDate) {
        if (getParentFragment() instanceof CalendarViewPagerFragment) {
            ((CalendarViewPagerFragment) getParentFragment()).addSelectedDate(calendarDate);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mProcessTask.setDateLoadListener(null);
    }

    public void updateList() {
        if (calendarGridViewAdapter != null) {
            calendarGridViewAdapter.updateList(mCalendarDateList, isSaturdayHoliday);
        }
    }

    public interface OnDateClickListener {
        void onDateClick(CalendarDate calendarDate);
    }

    public interface OnDateCancelListener {
        void onDateCancel(CalendarDate calendarDate);
    }

    private static class ProcessTask extends AsyncTask<Void, Void, List<CalendarDate>> {
        final int mYear;
        final int mMonth;
        final HashSet<String> mSelectedDates;
        private DateLoadListener mDateLoadListener;

        ProcessTask(int year, int month, HashSet<String> selectedDates) {
            mYear = year;
            mMonth = month;
            mSelectedDates = selectedDates;
        }

        public void setDateLoadListener(DateLoadListener dateLoadListener) {
            mDateLoadListener = dateLoadListener;
        }

        @Override
        protected List<CalendarDate> doInBackground(Void[] objects) {
            List<CalendarDate> mListDataCalendar;//日历数据
            mListDataCalendar = CalendarDateController.getCalendarDate(mYear, mMonth, mSelectedDates);
            return mListDataCalendar;
        }

        @Override
        protected void onPostExecute(List<CalendarDate> calendarDateList) {
            if (mDateLoadListener != null) {
                mDateLoadListener.onDateLoaded(calendarDateList);
            }
        }

        interface DateLoadListener {
            void onDateLoaded(List<CalendarDate> calendarDateList);
        }
    }
}
