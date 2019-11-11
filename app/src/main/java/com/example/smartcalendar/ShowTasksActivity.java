package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;

public class ShowTasksActivity extends AppCompatActivity {

    private Account account;
    private TextView taskBar;
    private Button button;
    private EditText editText;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_tasks);

        account = (Account) getIntent().getSerializableExtra("account");
        taskBar = findViewById(R.id.tasksBar);
        button = findViewById(R.id.deleteDoneButton);
        editText = findViewById(R.id.deleteTaskNo);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ii =  Integer.parseInt(editText.getText().toString().trim());
                account.deleteTask(ii-1);
                tasksDisplay();
                updateDB();
                editText.setText("");
            }
        });

        tasksDisplay();

        //taskBar.setText("hello");
    }

    public void tasksDisplay() {
        String string = "";
        ArrayList <Task> tasksList = account.getAllTasks();
        for(int i = 0; i < tasksList.size(); i++) {
            string += "Task " + (i + 1) + " : " + tasksList.get(i).getInfo();
        }
        Log.d("debug:" , string);
        taskBar.setText(string);
    }

    void updateDB() {
        ArrayList <Task> allTasks = account.getAllTasks();

        Collections.sort(allTasks, new dateCmp());
        int i;
        for(i = 0; i < allTasks.size(); i++) {
            databaseReference.child(firebaseUser.getUid()).child("tasks/task" + (i + 1)).setValue(allTasks.get(i));
        }
        databaseReference.child(firebaseUser.getUid()).child("taskCount").setValue(account.getTaskCount());

        databaseReference.child(firebaseUser.getUid()).child("tasks/task"+ (i+1)).setValue(null);

        Toast.makeText(this, "Task Deleted & saved", Toast.LENGTH_LONG).show();
    }


}
