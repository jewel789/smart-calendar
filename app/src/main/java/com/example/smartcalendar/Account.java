package com.example.smartcalendar;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    private String name;
    private int age;
    private int taskCount;

    private ArrayList <Task> allTasks = new ArrayList<>();

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

    public void addTask(Task task) {
        this.allTasks.add(task);
        this.taskCount++;
    }

    public void delTask() {
        this.taskCount--;
    }

    public ArrayList<Task> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(ArrayList<Task> allTasks) {
        this.allTasks = allTasks;
    }

    public void emptyTasks() {
        this.allTasks.clear();
        this.taskCount = 0;
    }
}
