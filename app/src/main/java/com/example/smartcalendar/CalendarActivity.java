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

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private Button logoutButton;
    private Button addTaskButton;
    private Button allTasksButton;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();

        logoutButton = findViewById(R.id.logout);
        addTaskButton = findViewById(R.id.addTask);
        allTasksButton = findViewById(R.id.allTasks);

        logoutButton.setOnClickListener(this);
        addTaskButton.setOnClickListener(this);
        allTasksButton.setOnClickListener(this);
    }

    private void showData(DataSnapshot ds) {
        account = new Account();
        account.setName(ds.child(user.getUid()).getValue(Account.class).getName());
        account.setAge(ds.child(user.getUid()).getValue(Account.class).getAge());
        account.setTaskcount(ds.child(user.getUid()).getValue(Account.class).getTaskcount());
        Toast.makeText(this, "Welcome " + account.getName() + " (Aged : " + account.getAge() + ")", Toast.LENGTH_LONG).show();
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
    }
}
