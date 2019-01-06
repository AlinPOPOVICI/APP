package com.example.alin.app1;

public class Aplicatie {
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
