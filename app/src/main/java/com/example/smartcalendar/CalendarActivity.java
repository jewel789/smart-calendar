package com.example.smartcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {

    private FirebaseAuth firebaseAuth;

    private Button logoutButton;
    private Button addTaskButton;
    private Button allTasksButton;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

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
                    account = dataSnapshot.getValue(Account.class);
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

        CalendarView calendarView = findViewById(R.id.Calendar);
        calendarView.setOnDateChangeListener(this);
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
            if(!TextUtils.isEmpty(account.getName())) {
                Intent intent = new Intent(this, TaskAddActivity.class);
                intent.putExtra("account", account);
                startActivity(intent);
            }
        }

        if(view == allTasksButton) {
            if(!TextUtils.isEmpty(account.getName())) {
                Intent intent = new Intent(this, ShowTasksActivity.class);
                intent.putExtra("account", account);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(dayOfMonth + "/" + (month + 1) + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(account.getName())) {
            Intent intent = new Intent(this, ShowTasksActivity.class);
            intent.putExtra("account", account);
            intent.putExtra("date", date);
            startActivity(intent);
        }
    }
}