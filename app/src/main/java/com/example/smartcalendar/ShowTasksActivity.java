package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings("ALL")
public class ShowTasksActivity extends AppCompatActivity implements View.OnClickListener {

    private Account account;

    private ListView tasksListView;

    private DatabaseReference reference;

    private Button searchButton;
    private EditText searchBox;

    public static final long HOUR = 3600*1000;

    private ArrayList <Task> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tasks);

        account = (Account) getIntent().getSerializableExtra("account");
        Date date = (Date) getIntent().getSerializableExtra("date");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user).getUid());

        tasksListView = findViewById(R.id.tasks_list);
        searchButton = findViewById(R.id.searchButton);
        searchBox = findViewById(R.id.searchBox);

        searchButton.setOnClickListener(this);

        if(date != null) {
            findDatedTasks(date);
        }
        else {
            findAllTasks();
        }
    }

    private void clearOldTasks() {
        tasksList = account.getAllTasks();
        Collections.sort(tasksList, new dateCmp());

        Date date = new Date();

        while(tasksList.size() > 0) {
            if(tasksList.get(0).getDate().compareTo(date) <= 0) {
                Task task = account.getAllTasks().get(0);
                if (!task.getRepeat().equals("None")) {
                    Date newDate = null;
                    if (task.getRepeat().equals("Daily"))
                        newDate = new Date(task.getDate().getTime() + 5 * 24 * HOUR);
                    else if (task.getRepeat().equals("Weekly"))
                        newDate = new Date(task.getDate().getTime() + 5 * 24 * 7 * HOUR);

                    Task newTask = new Task(task.getName(), newDate, task.getRepeat());
                    account.addTask(newTask);
                    setAlarm(newTask);
                    Collections.sort(tasksList, new dateCmp());
                }
                account.delTask();
            }
            else break;
        }

        reference.setValue(account);
    }

    private void findAllTasks() {
        clearOldTasks();
        tasksList = account.getAllTasks();
        ArrayList <String> tasksInfo = new ArrayList<>();
        ArrayList <Integer> taskMap = new ArrayList<>();

        for(int i = 0; i < tasksList.size(); i++) {
            tasksInfo.add("Task " + (i + 1) + " : " + tasksList.get(i).getInfo());
            taskMap.add(i);
        }

        if(tasksList.isEmpty()) Toast.makeText(this, "You have no pending tasks!", Toast.LENGTH_SHORT).show();

        tasksDisplay(tasksInfo, taskMap);
    }

    private void findMatchingTasks(CharSequence str) {
        clearOldTasks();
        ArrayList <String> tasksInfo = new ArrayList<>();
        ArrayList <Integer> taskMap = new ArrayList<>();

        for(int i = 0; i < tasksList.size(); i++) {
            String name = tasksList.get(i).getName().toLowerCase();
            if(name.contains(str)) {
                tasksInfo.add("Task " + (i + 1) + " : " + tasksList.get(i).getInfo());
                taskMap.add(i);
            }
        }

        if(tasksInfo.isEmpty()) Toast.makeText(this, "You have no matching tasks!", Toast.LENGTH_SHORT).show();

        tasksDisplay(tasksInfo, taskMap);
    }

    private void findDatedTasks(Date date) {
        clearOldTasks();
        ArrayList <String> tasksInfo = new ArrayList<>();
        ArrayList <Integer> taskMap = new ArrayList<>();

        for(int i = 0; i < tasksList.size(); i++) {
            Date taskDate = tasksList.get(i).getDate();

            if(taskDate.getDate() == date.getDate() && taskDate.getMonth() == date.getMonth() && taskDate.getYear() == date.getYear()) {
                tasksInfo.add("Task " + (i + 1) + " : " + tasksList.get(i).getInfo());
                taskMap.add(i);
            }
        }

        if(tasksInfo.isEmpty()) Toast.makeText(this, "You have no tasks on this day!", Toast.LENGTH_SHORT).show();

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

    @SuppressLint("NewApi")
    private void setAlarm(Task task) {
        if(task.isAlarm()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(task.getDate());

            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager  = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if(alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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