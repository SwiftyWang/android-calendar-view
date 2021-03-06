package com.joybar.librarycalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joybar.librarycalendar.R;
import com.joybar.librarycalendar.data.CalendarDate;
import com.joybar.librarycalendar.data.Lunar;
import com.joybar.librarycalendar.utils.DateUtils;

import java.util.List;


/**
 * Created by joybar on 2/24/16.
 */
public class CalendarGridViewAdapter extends BaseAdapter {

    private boolean mIsSaturdayHoliday;
    private List<CalendarDate> mListData;


    public CalendarGridViewAdapter(List<CalendarDate> mListData, boolean isSaturdayHoliday) {
        this.mListData = mListData;
        mIsSaturdayHoliday = isSaturdayHoliday;
    }

    public List<CalendarDate> getListData() {
        return mListData;
    }


    public int getCount() {
        return mListData.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        CalendarDate calendarDate = mListData.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_calendar, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_day.setText(String.valueOf(calendarDate.getSolar().solarDay));

        String str;

        boolean isFestival = false;
        if (!TextUtils.isEmpty(calendarDate.getSolar().solarFestivalName)) {
            str = calendarDate.getSolar().solarFestivalName;
            isFestival = true;
        } else if (!TextUtils.isEmpty(calendarDate.getLunar().lunarFestivalName)) {
            str = calendarDate.getLunar().lunarFestivalName;
            isFestival = true;
        } else if (!TextUtils.isEmpty(calendarDate.getSolar().solar24Term)) {
            str = calendarDate.getSolar().solar24Term;
        } else {
            str = Lunar.getChinaDayString(calendarDate.getLunar().lunarDay);
        }
        viewHolder.tv_lunar_day.setText(str);
        if (calendarDate.isInThisMonth()) {
            if (position % 7 == 0 || isFestival || (mIsSaturdayHoliday && position % 7 == 6)) {
                viewHolder.tv_day.setTextColor(Color.RED);
            } else {
                viewHolder.tv_day.setTextColor(Color.BLACK);
            }
        } else {
            viewHolder.tv_day.setTextColor(Color.LTGRAY);
            viewHolder.tv_lunar_day.setTextColor(Color.LTGRAY);
        }
        if (DateUtils.isToday(calendarDate)) {
            viewHolder.tv_day.setBackgroundResource(R.drawable.red_circle);
            viewHolder.tv_day.setTextColor(Color.WHITE);
        } else {
            viewHolder.tv_day.setBackgroundColor(0);
        }
        return convertView;
    }

    public void updateList(List<CalendarDate> calendarDates, boolean isSaturdayHoliday) {
        mIsSaturdayHoliday = isSaturdayHoliday;
        mListData = calendarDates;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        private TextView tv_day;
        private TextView tv_lunar_day;

        public ViewHolder(View itemView) {
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_lunar_day = (TextView) itemView.findViewById(R.id.tv_lunar_day);
        }

    }
}

