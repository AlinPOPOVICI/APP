package com.example.alin.app1.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.alin.app1.DateConverter;

import java.util.Date;

@Entity(tableName = "AplicatieData")
public class AplicatieData {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters(DateConverter.class)
    private Date time;

    @ColumnInfo(name = "app_name")
    private String name;


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

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }




}

