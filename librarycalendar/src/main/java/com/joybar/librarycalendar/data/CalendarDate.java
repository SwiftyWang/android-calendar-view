package com.joybar.librarycalendar.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joybar on 2/24/16.
 */
public class CalendarDate implements Parcelable {

    public static final Creator<CalendarDate> CREATOR = new Creator<CalendarDate>() {
        @Override
        public CalendarDate createFromParcel(Parcel source) {
            return new CalendarDate(source);
        }

        @Override
        public CalendarDate[] newArray(int size) {
            return new CalendarDate[size];
        }
    };
    private Lunar lunar = new Lunar();//农历
    private Solar solar = new Solar();//公历
    private boolean isInThisMonth; //是否在当月
    private SelectStatus mSelectStatus;//是否被选中

    public CalendarDate(int year, int month, int day, boolean isInThisMonth, SelectStatus selectStatus, Lunar lunar) {
        this.isInThisMonth = isInThisMonth;
        this.mSelectStatus = selectStatus;
        this.lunar = lunar;
    }

    public CalendarDate(boolean isInThisMonth, SelectStatus selectStatus, Solar solar, Lunar lunar) {
        this.isInThisMonth = isInThisMonth;
        this.mSelectStatus = selectStatus;
        this.solar = solar;
        this.lunar = lunar;
    }

    protected CalendarDate(Parcel in) {
        this.lunar = in.readParcelable(Lunar.class.getClassLoader());
        this.solar = in.readParcelable(Solar.class.getClassLoader());
        this.isInThisMonth = in.readByte() != 0;
        int tmpMSelectStatus = in.readInt();
        this.mSelectStatus = tmpMSelectStatus == -1 ? null : SelectStatus.values()[tmpMSelectStatus];
    }

    public boolean isInThisMonth() {
        return isInThisMonth;
    }

    public void setInThisMonth(boolean inThisMonth) {
        isInThisMonth = inThisMonth;
    }

    public void setIsInThisMonth(boolean isInThisMonth) {
        this.isInThisMonth = isInThisMonth;
    }

    public Solar getSolar() {
        return solar;
    }

    public void setSolar(Solar solar) {
        this.solar = solar;
    }

    public Lunar getLunar() {
        return lunar;
    }

    public void setLunar(Lunar lunar) {
        this.lunar = lunar;
    }

    /**
     * @return eg. 2018/04/10, 2020/01/01, 2019/12/25
     */
    public String getSolarDateString() {
        return solar.getDateString();
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
        dest.writeInt(this.mSelectStatus == null ? -1 : this.mSelectStatus.ordinal());
    }

    public SelectStatus nextSelectStatus() {
        mSelectStatus = SelectStatus.values()[((mSelectStatus.ordinal() + 1) % SelectStatus.values().length)];
        return mSelectStatus;
    }

    public enum SelectStatus {
        NotSelect,
        SelectMorning,
        SelectAfternoon,
        SelectWhole,
    }
}
