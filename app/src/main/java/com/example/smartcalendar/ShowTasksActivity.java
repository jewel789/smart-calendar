package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        tasksDisplay();
    }

    private void tasksDisplay() {
        ArrayList <Task> tasksList = account.getAllTasks();
        Collections.sort(tasksList, new dateCmp());

        Date date = new Date();
        ArrayList <String> tasksInfo = new ArrayList<>();

        while(tasksList.size() > 0) {
            if(tasksList.get(0).getDate().compareTo(date) <= 0) {
                account.delTask();
            }
            else break;
        }
        for(int i = 0; i < tasksList.size(); i++) {
            tasksInfo.add("Task " + (i + 1) + " : " + tasksList.get(i).getInfo());
        }
        reference.setValue(account);

        if(tasksList.isEmpty()) Toast.makeText(this, "You have no pending tasks!", Toast.LENGTH_LONG).show();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasksInfo);
        tasksListView.setAdapter(arrayAdapter);
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowTasksActivity.this, EditTaskActivity.class);
                intent.putExtra("account", account);
                intent.putExtra("taskPos", position);
                startActivity(intent);
                finish();
                //Toast.makeText(ShowTasksActivity.this, "task number - " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class dateCmp implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return t1.getDate().compareTo(t2.getDate());
    }
}