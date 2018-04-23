package com.joybar.librarycalendar.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.joybar.librarycalendar.R;
import com.joybar.librarycalendar.adapter.CalendarGridViewAdapter;
import com.joybar.librarycalendar.controller.CalendarDateController;
import com.joybar.librarycalendar.data.CalendarDate;
import com.joybar.librarycalendar.utils.DateUtils;

import java.util.HashSet;
import java.util.List;


/**
 * Created by joybar on 4/27/16.
 */
public class CalendarViewFragment extends Fragment {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String CHOICE_MODE_SINGLE = "choice_mode_single";
    private boolean isChoiceModelSingle;
    private View mProgress;
    private int mYear;
    private int mMonth;
    private GridView mGridView;
    private OnDateClickListener onDateClickListener;
    private OnDateCancelListener onDateCancelListener;
    private HashSet<String> selectedDates;
    private ProcessTask mProcessTask;

    public CalendarViewFragment() {
    }

    public static CalendarViewFragment newInstance(int year, int month) {
        CalendarViewFragment fragment = new CalendarViewFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        fragment.setArguments(args);
        return fragment;
    }

    public static CalendarViewFragment newInstance(int year, int month, boolean isChoiceModelSingle, HashSet<String> selectedDates) {
        CalendarViewFragment fragment = new CalendarViewFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        args.putBoolean(CHOICE_MODE_SINGLE, isChoiceModelSingle);
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
        return view;
    }

    private final ProcessTask.DateLoadListener mDateLoadListener = new ProcessTask.DateLoadListener() {

        @Override
        public void onDateLoaded(final List<CalendarDate> calendarDateList) {
            mProgress.setVisibility(View.GONE);
            mGridView.setAdapter(new CalendarGridViewAdapter(calendarDateList));
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
                        if (calendarDateList.get(position).isInThisMonth()) {
                            addSelectedDate(calendarDate);
                            onDateClickListener.onDateClick(calendarDate);
                        } else {
                            mGridView.setItemChecked(position, false);
                        }
                    } else {
                        //多选
                        if (calendarDateList.get(position).isInThisMonth()) {
                            // mGridView.getCheckedItemIds()
                            if (!mGridView.isItemChecked(position)) {
                                removeSelectedDate(calendarDate);
                                onDateCancelListener.onDateCancel(calendarDate);
                            } else {
                                addSelectedDate(calendarDate);
                                onDateClickListener.onDateClick(calendarDate);
                            }

                        } else {
                            mGridView.setItemChecked(position, false);
                        }

                    }
                }
            });
            mGridView.post(new Runnable() {
                @Override
                public void run() {
                    //需要默认选中当天
                    List<CalendarDate> mListData = ((CalendarGridViewAdapter) mGridView.getAdapter()).getListData();
                    int count = mListData.size();
                    for (int i = 0; i < count; i++) {
                        if (mListData.get(i).isSelect()) {
                            mGridView.setItemChecked(i, true);
                        } else if (mListData.get(i).getSolar().solarDay == DateUtils.getDay()
                                && mListData.get(i).getSolar().solarMonth == DateUtils.getMonth()
                                && mListData.get(i).getSolar().solarYear == DateUtils.getYear()) {
                            if (null != mGridView.getChildAt(i) && mListData.get(i).isInThisMonth()) {
                                mGridView.setItemChecked(i, true);
                            }
                        }
                    }

                }
            });
        }
    };

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

        public void setDateLoadListener(DateLoadListener dateLoadListener) {
            mDateLoadListener = dateLoadListener;
        }

        private DateLoadListener mDateLoadListener;

        interface DateLoadListener {
            void onDateLoaded(List<CalendarDate> calendarDateList);
        }

        ProcessTask(int year, int month, HashSet<String> selectedDates) {
            mYear = year;
            mMonth = month;
            mSelectedDates = selectedDates;
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
    }
}
