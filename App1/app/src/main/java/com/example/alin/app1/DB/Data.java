package com.example.alin.app1.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.alin.app1.DateConverter;

import java.util.Date;

@Entity(tableName = "Data")
public class Data implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters(DateConverter.class)
    private Date time;

    @ColumnInfo (name = "headphone_state")
    private int headphoneState;

    @ColumnInfo (name = "weather_condition")
    private int weatherCondition;

    @ColumnInfo (name = "weather_celsius")
    private float weatherCelsius;

    @ColumnInfo (name = "activity")
    private int activity;

    @ColumnInfo (name = "location_latitude")
    private double locationLatitude;

    @ColumnInfo (name = "location_longitude")
    private double locationLongitude;

    @ColumnInfo (name = "app_name")
    private String app_name;

    public Data(){

    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    protected Data(Parcel in) {
        id = in.readInt();
        headphoneState = in.readInt();
        weatherCondition = in.readInt();
        weatherCelsius = in.readFloat();
        activity = in.readInt();
        locationLatitude = in.readDouble();
        locationLongitude = in.readDouble();
        app_name = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public int getHeadphoneState() {
        return headphoneState;
    }

    public void setHeadphoneState(int headphoneState) {
        this.headphoneState = headphoneState;
    }

    public int getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(int weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public float getWeatherCelsius() {
        return weatherCelsius;
    }

    public void setWeatherCelsius(float weatherCelsius) {
        this.weatherCelsius = weatherCelsius;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(headphoneState);
        dest.writeInt(weatherCondition);
        dest.writeFloat(weatherCelsius);
        dest.writeInt(activity);
        dest.writeDouble(locationLatitude);
        dest.writeDouble(locationLongitude);
        dest.writeString(app_name);
    }
}
