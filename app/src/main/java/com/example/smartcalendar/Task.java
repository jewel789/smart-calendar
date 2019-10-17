package com.example.smartcalendar;

import java.io.Serializable;


public class Task implements Serializable {
    private String name;
    private String Date;
    private String Time;

    public  Task(){

     }
    public Task(String time,String date,String name) {
        this.name = name;
        this.Date=date;
        this.Time=time;
    }



    public String getTime() { return Time; }
    public void setTime(String time) { this.Time = time; }

    public String getDate() {   return Date; }
    public void setDate(String date) { this.Date = date; }

    public String getName() { return name;}
    public void setName(String name) { this.name = name; }

}
