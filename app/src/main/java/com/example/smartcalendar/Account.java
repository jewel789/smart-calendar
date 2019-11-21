package com.example.smartcalendar;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    private String name;
    private int age;

    private ArrayList <Task> allTasks = new ArrayList<>();
    private ArrayList<Task> oldTasks = new ArrayList<>();

    public ArrayList<Task> getOldTasks() {
        return oldTasks;
    }

    public void setOldTasks(ArrayList<Task> oldTasks) {
        this.oldTasks = oldTasks;
    }

    public Account() {}

    public Account(String name, int age) {
        this.name = name;
        this.age = age;
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

    public void addTask(Task task) {
        this.allTasks.add(task);
    }

    public void delTask() {
        if(!this.allTasks.isEmpty()) {
            this.allTasks.remove(0);
        }
    }

    public ArrayList<Task> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(ArrayList<Task> allTasks) {
        this.allTasks = allTasks;
    }

    public void emptyTasks() {
        this.allTasks.clear();
    }
}
