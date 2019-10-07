package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();

        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == logoutButton) {
            firebaseAuth.signOut();
            Toast.makeText(this, "Goodbye, you are now logged out", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
