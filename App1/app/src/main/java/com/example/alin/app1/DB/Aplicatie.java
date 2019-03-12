package com.example.alin.app1.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.alin.app1.DateConverter;

import java.util.Date;

@Entity(tableName = "Aplicatie")
public class Aplicatie{

    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters(DateConverter.class)
    private Date time;

    @ColumnInfo(name = "app_name")
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


}
