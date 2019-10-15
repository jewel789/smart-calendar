package com.example.smartcalendar;

import java.io.Serializable;

public class Task implements Serializable {
    private String name;

    public Task() {}

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
