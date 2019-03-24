package com.example.alin.app1.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Aplicatie")
public class Aplicatie{

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "prioritate")
    private int prioritate;

    @ColumnInfo(name = "app_name")
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }


    public int getPrioritate() {
        return prioritate;
    }

    public void setPrioritate(int prioritate) {
        this.prioritate = prioritate;
    }
}
