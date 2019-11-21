package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class ShowTasksActivity extends AppCompatActivity implements View.OnClickListener {

    private Account account;

    private ListView tasksListView;

    private DatabaseReference reference;

    private Button searchButton;
    private EditText searchBox;

    private ArrayList <Task> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tasks);

        account = (Account) getIntent().getSerializableExtra("account");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user).getUid());

        tasksListView = findViewById(R.id.tasks_list);
        searchButton = findViewById(R.id.searchButton);
        searchBox = findViewById(R.id.searchBox);

        searchButton.setOnClickListener(this);

        findAllTasks();
    }

    private void clearOldTasks() {
        tasksList = account.getAllTasks();
        Collections.sort(tasksList, new dateCmp());

        Date date = new Date();

        while(tasksList.size() > 0) {
            if(tasksList.get(0).getDate().compareTo(date) <= 0) {
                account.delTask();
            }
            else break;
        }

        reference.setValue(account);
    }

    private void findAllTasks() {
        //clearOldTasks();
        tasksList = account.getAllTasks();
        ArrayList <String> tasksInfo = new ArrayList<>();
        ArrayList <Integer> taskMap = new ArrayList<>();

        for(int i = 0; i < tasksList.size(); i++) {
            tasksInfo.add("Task " + (i + 1) + " : " + tasksList.get(i).getInfo());
            taskMap.add(i);
        }

        if(tasksList.isEmpty()) Toast.makeText(this, "You have no pending tasks!", Toast.LENGTH_LONG).show();

        tasksDisplay(tasksInfo, taskMap);
    }

    private void findMatchingTasks(CharSequence str) {
        //clearOldTasks();
        ArrayList <String> tasksInfo = new ArrayList<>();
        ArrayList <Integer> taskMap = new ArrayList<>();

        for(int i = 0; i < tasksList.size(); i++) {
            String name = tasksList.get(i).getName().toLowerCase();
            if(name.contains(str)) {
                tasksInfo.add("Task " + (i + 1) + " : " + tasksList.get(i).getInfo());
                taskMap.add(i);
            }
        }

        if(tasksList.isEmpty()) Toast.makeText(this, "You have no matching tasks!", Toast.LENGTH_LONG).show();

        tasksDisplay(tasksInfo, taskMap);
    }

    private void tasksDisplay(ArrayList <String> tasksInfo, final ArrayList <Integer> taskMap) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasksInfo);
        tasksListView.setAdapter(arrayAdapter);
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowTasksActivity.this, EditTaskActivity.class);
                intent.putExtra("account", account);
                intent.putExtra("taskPos", taskMap.get(position));
                startActivity(intent);
                finish();
                //Toast.makeText(ShowTasksActivity.this, "task number - " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == searchButton) {
            String str = searchBox.getText().toString().toLowerCase();
            if(!TextUtils.isEmpty(str)) {
                findMatchingTasks(str);
            }
            else {
                findAllTasks();
            }
        }
    }
}

class dateCmp implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return t1.getDate().compareTo(t2.getDate());
    }
}