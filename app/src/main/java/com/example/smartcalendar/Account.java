package com.example.smartcalendar;

import java.io.Serializable;

public class Account implements Serializable {
    private String name;
    private int age;
    private int taskCount;

    public Account() {}

    public Account(String name, int age) {
        this.name = name;
        this.age = age;
        this.taskCount = 0;
    }

    public Account(String name, int age, int taskCount) {
        this.name = name;
        this.age = age;
        this.taskCount = taskCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTaskcount() {
        return taskCount;
    }

    public void setTaskcount(int taskCount) {
        this.taskCount = taskCount;
    }

    public void addTask() {
        this.taskCount++;
    }

    public void delTask() {
        this.taskCount--;
    }
}
