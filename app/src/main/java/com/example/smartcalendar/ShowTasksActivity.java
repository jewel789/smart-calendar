package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowTasksActivity extends AppCompatActivity {

    private Account account;

    private TextView taskBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_tasks);

        account = (Account) getIntent().getSerializableExtra("account");

        taskBar = findViewById(R.id.tasksBar);

        tasksDisplay();
    }

    public void tasksDisplay() {
        String string = "";
        ArrayList <Task> tasksList = account.getAllTasks();
        for(int i = 0; i < tasksList.size(); i++) {
            string += "Task " + (i + 1) + " : " + tasksList.get(i).getTime()+" " +tasksList.get(i).getDate()+tasksList.get(i).getName() + "\n";


        }
        taskBar.setText(string);
    }
}
