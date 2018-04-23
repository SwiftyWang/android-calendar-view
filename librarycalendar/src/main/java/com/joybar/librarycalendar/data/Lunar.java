package com.joybar.librarycalendar.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joybar on 9/12/16.
 */
public class Lunar implements Parcelable {
    public boolean isleap;
    public int lunarDay;
    public int lunarMonth;
    public int lunarYear;
    public boolean isLFestival;
    public String lunarFestivalName;//农历节日


    final static String chineseNumber[] =
            {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    public static String getChinaDayString(int day) {
        String chineseTen[] =
                {"初", "十", "廿", "卅"};
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30)
            return "";
        if (day == 10)
            return "初十";
        else
            return chineseTen[day / 10] + chineseNumber[n];
    }

    @Override
    public String toString() {
        return "Lunar [isleap=" + isleap + ", lunarDay=" + lunarDay
                + ", lunarMonth=" + lunarMonth + ", lunarYear=" + lunarYear
                + ", isLFestival=" + isLFestival + ", lunarFestivalName="
                + lunarFestivalName + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isleap ? (byte) 1 : (byte) 0);
        dest.writeInt(this.lunarDay);
        dest.writeInt(this.lunarMonth);
        dest.writeInt(this.lunarYear);
        dest.writeByte(this.isLFestival ? (byte) 1 : (byte) 0);
        dest.writeString(this.lunarFestivalName);
    }

    public Lunar() {
    }

    protected Lunar(Parcel in) {
        this.isleap = in.readByte() != 0;
        this.lunarDay = in.readInt();
        this.lunarMonth = in.readInt();
        this.lunarYear = in.readInt();
        this.isLFestival = in.readByte() != 0;
        this.lunarFestivalName = in.readString();
    }

    public static final Parcelable.Creator<Lunar> CREATOR = new Parcelable.Creator<Lunar>() {
        @Override
        public Lunar createFromParcel(Parcel source) {
            return new Lunar(source);
        }

        @Override
        public Lunar[] newArray(int size) {
            return new Lunar[size];
        }
    };
}
