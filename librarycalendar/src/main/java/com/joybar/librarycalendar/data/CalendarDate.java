package com.joybar.librarycalendar.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joybar on 2/24/16.
 */
public class CalendarDate implements Parcelable {

    private Lunar lunar = new Lunar();//农历
    private Solar solar = new Solar();//公历
    private boolean isInThisMonth; //是否在当月
    private boolean isSelect;//是否被选中

    public CalendarDate(int year, int month, int day, boolean isInThisMonth, boolean isSelect, Lunar lunar) {
        this.isInThisMonth = isInThisMonth;
        this.isSelect = isSelect;
        this.lunar = lunar;
    }


    public CalendarDate(boolean isInThisMonth, boolean isSelect, Solar solar, Lunar lunar) {
        this.isInThisMonth = isInThisMonth;
        this.isSelect = isSelect;
        this.solar = solar;
        this.lunar = lunar;
    }

    public boolean isInThisMonth() {
        return isInThisMonth;
    }

    public void setIsInThisMonth(boolean isInThisMonth) {
        this.isInThisMonth = isInThisMonth;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Solar getSolar() {
        return solar;
    }

    public void setSolar(Solar solar) {
        this.solar = solar;
    }

    public void setInThisMonth(boolean inThisMonth) {
        isInThisMonth = inThisMonth;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Lunar getLunar() {
        return lunar;
    }

    public void setLunar(Lunar lunar) {
        this.lunar = lunar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.lunar, flags);
        dest.writeParcelable(this.solar, flags);
        dest.writeByte(this.isInThisMonth ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected CalendarDate(Parcel in) {
        this.lunar = in.readParcelable(Lunar.class.getClassLoader());
        this.solar = in.readParcelable(Solar.class.getClassLoader());
        this.isInThisMonth = in.readByte() != 0;
        this.isSelect = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CalendarDate> CREATOR = new Parcelable.Creator<CalendarDate>() {
        @Override
        public CalendarDate createFromParcel(Parcel source) {
            return new CalendarDate(source);
        }

        @Override
        public CalendarDate[] newArray(int size) {
            return new CalendarDate[size];
        }
    };

    /**
     *
     * @return eg. 2018/4/10
     */
    public String getSolarDateString() {
        return solar.solarYear + "/" + solar.solarMonth + "/" + solar.solarDay;
    }
}
