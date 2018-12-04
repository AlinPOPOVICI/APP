package com.example.alin.app1;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "Data")
public class Data {


    @PrimaryKey(autoGenerate = true)
    public int id;


    @TypeConverters(DateConverter.class)
    private Date time;

    @ColumnInfo (name = "time_interval")
    private int timeInterval;

    @ColumnInfo (name = "headphone_state")
    private int headphoneState;

    @ColumnInfo (name = "weather_condition")
    private int weatherCondition;

    @ColumnInfo (name = "weather_celsius")
    private int weatherCelsius;

    @ColumnInfo (name = "activity")
    private int activity;

    @ColumnInfo (name = "location_latitude")
    private int locationLatitude;

    @ColumnInfo (name = "location_longitude")
    private int locationLongitude;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
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

    public int getWeatherCelsius() {
        return weatherCelsius;
    }

    public void setWeatherCelsius(int weatherCelsius) {
        this.weatherCelsius = weatherCelsius;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(int locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public int getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(int locationLongitude) {
        this.locationLongitude = locationLongitude;
    }


}
