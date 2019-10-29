package com.example.smartcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Button logoutButton;
    private Button addTaskButton;
    private Button allTasksButton;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        account = new Account();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    showData(dataSnapshot);
                }
                else {
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        logoutButton = findViewById(R.id.logout);
        addTaskButton = findViewById(R.id.addTask);
        allTasksButton = findViewById(R.id.allTasks);

        logoutButton.setOnClickListener(this);
        addTaskButton.setOnClickListener(this);
        allTasksButton.setOnClickListener(this);
    }

    private void showData(DataSnapshot ds) {
        account.setName(ds.getValue(Account.class).getName());
        account.setAge(ds.getValue(Account.class).getAge());
        account.setTaskcount(ds.getValue(Account.class).getTaskcount());

        ArrayList <Task> allTasks = new ArrayList<>();
        for (int i = 1; i <= account.getTaskcount(); i++) {
            Task task = ds.child("tasks/task" + i).getValue(Task.class);
            //task.setName(ds.child("tasks/task" + i).getValue(Task.class).getName());
            allTasks.add(task);
        }
        account.setAllTasks(allTasks);
    }

    @Override
    public void onClick(View view) {

        if(view == logoutButton) {
            firebaseAuth.signOut();
            Toast.makeText(this, "Goodbye " + account.getName() + ", you are now logged out", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == addTaskButton) {
            Intent intent = new Intent(this, TaskAddActivity.class);
            intent.putExtra("account", account);
            startActivity(intent);
        }

        if(view == allTasksButton) {
            Intent intent = new Intent(this, ShowTasksActivity.class);
            intent.putExtra("account", account);
            startActivity(intent);
        }
    }
}
