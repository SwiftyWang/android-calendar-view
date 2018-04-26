package com.joybar.librarycalendar.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by joybar on 9/12/16.
 */
public class Solar implements Parcelable {
    public static final Parcelable.Creator<Solar> CREATOR = new Parcelable.Creator<Solar>() {
        @Override
        public Solar createFromParcel(Parcel source) {
            return new Solar(source);
        }

        @Override
        public Solar[] newArray(int size) {
            return new Solar[size];
        }
    };
    public int solarDay;
    public int solarMonth;
    public int solarYear;
    public boolean isSFestival;
    public String solarFestivalName;//公历节日
    public String solar24Term;//24节气

    public Solar() {
    }

    protected Solar(Parcel in) {
        this.solarDay = in.readInt();
        this.solarMonth = in.readInt();
        this.solarYear = in.readInt();
        this.isSFestival = in.readByte() != 0;
        this.solarFestivalName = in.readString();
        this.solar24Term = in.readString();
    }

    @Override
    public String toString() {
        return "Solar [solarDay=" + solarDay + ", solarMonth=" + solarMonth
                + ", solarYear=" + solarYear + ", isSFestival=" + isSFestival
                + ", solarFestivalName=" + solarFestivalName + ", solar24Term="
                + solar24Term + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.solarDay);
        dest.writeInt(this.solarMonth);
        dest.writeInt(this.solarYear);
        dest.writeByte(this.isSFestival ? (byte) 1 : (byte) 0);
        dest.writeString(this.solarFestivalName);
        dest.writeString(this.solar24Term);
    }

    /**
     * @return eg. 2018/04/10, 2020/01/01, 2019/12/25
     */
    public String getDateString() {
        return solarYear + "/" + String.format(Locale.getDefault(), "%02d", solarMonth) + "/" + String.format(Locale.getDefault(), "%02d", solarDay);
    }
}
