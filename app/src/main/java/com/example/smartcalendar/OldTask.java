package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OldTask extends AppCompatActivity {

   ListView listView;
   Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_task);

        listView = findViewById(R.id.old_tasks_list);
        account = (Account) getIntent().getSerializableExtra("account");


        assert account != null;
        ArrayList<Task> tasksList = account.getOldTasks();

        ArrayList<String> tasksInfo = new ArrayList<>();
        final ArrayList <Integer> taskMap = new ArrayList<>();

        for(int i = 0; i < tasksList.size(); i++) {
            tasksInfo.add("Task " + (i + 1) + " : " + tasksList.get(i).getInfo());
            taskMap.add(i);
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasksInfo);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OldTask.this, EditTaskActivity.class);
                intent.putExtra("account", account);
                intent.putExtra("taskPos", taskMap.get(position));
                startActivity(intent);
                finish();
                //Toast.makeText(ShowTasksActivity.this, "task number - " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
