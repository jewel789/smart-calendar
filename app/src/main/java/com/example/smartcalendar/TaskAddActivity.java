package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.media.audiofx.AcousticEchoCanceler;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskAddActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private EditText taskName;
    private Button addTaskButton;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        account = (Account) getIntent().getSerializableExtra("account");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        taskName = findViewById(R.id.taskName);
        addTaskButton = findViewById(R.id.addTaskButton);

        addTaskButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == addTaskButton) {
            String name = taskName.getText().toString().trim();
            if(!TextUtils.isEmpty(name)) {
                Task task = new Task(name);
                account.addTask();
                databaseReference.child(user.getUid()).child("taskcount").setValue(account.getTaskcount());
                databaseReference.child(user.getUid()).child("tasks/task" + account.getTaskcount()).setValue(task);
                Toast.makeText(this, "Task added & saved", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
