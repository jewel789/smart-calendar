package com.example.smartcalendar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Task implements Serializable {
    private String name;
    private Date date;

    public Task(){

    }

    public Task(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    public String getInfo() {
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy  hh:mm a", Locale.getDefault());
        now += sdf.format(this.date) + " - "  + this.name + '\n';
        return now;
    }
}
