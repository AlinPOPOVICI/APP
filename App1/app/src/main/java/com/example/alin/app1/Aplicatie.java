package com.example.alin.app1;

import android.arch.persistence.room.Entity;

import java.io.Serializable;

@Entity(tableName = "Aplicatie")
public class Aplicatie implements Serializable {
    public Aplicatie(String Name) {
        this.title = Name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private String title;
    private Data data;

}
