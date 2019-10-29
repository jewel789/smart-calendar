package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ShowTasksActivity extends AppCompatActivity {

    private Account account;

    private ListView tasksListView;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tasks);

        account = (Account) getIntent().getSerializableExtra("account");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user).getUid());

        tasksListView = findViewById(R.id.tasks_list);

        tasksClear();
    }

    private void tasksClear() {
        Date date = new Date();
        ArrayList <Task> tasksList = account.getAllTasks();
        for(int i = 0; i < tasksList.size(); i++) {
            if(tasksList.get(i).getDate().compareTo(date) > 0) {
                for(int j = i; j < tasksList.size(); j++) {
                    reference.child("tasks/task" + (j - i + 1)).setValue(tasksList.get(j));
                }
                break;
            }
            else {
                reference.child("taskcount").setValue(tasksList.size() - i - 1);
                reference.child("tasks/task" + (tasksList.size() - i)).removeValue();
            }
        }
        tasksDisplay();
    }

    private void tasksDisplay() {
        ArrayList <Task> tasksList = account.getAllTasks();
        ArrayList <String> tasksInfo = new ArrayList<>();
        for(int i = 0; i < tasksList.size(); i++) {
            tasksInfo.add("Task " + (i + 1) + " : " + tasksList.get(i).getInfo());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasksInfo);
        tasksListView.setAdapter(arrayAdapter);
    }
}
