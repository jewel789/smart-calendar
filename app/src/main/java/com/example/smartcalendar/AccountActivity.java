package com.example.smartcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener{

    private Button accountContinue;
    private EditText accName;
    private EditText accAge;

    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountContinue = findViewById(R.id.accountContinue);
        accName = findViewById(R.id.accountName);
        accAge = findViewById(R.id.accountAge);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference(user.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        accountContinue.setOnClickListener(this);
    }

    private void createAccount() {
        String name = accName.getText().toString().trim();
        int age = Integer.parseInt(accAge.getText().toString());

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(Integer.toString(age))) {
            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }

        Account account = new Account(name, age);
        ref.setValue(account);

        finish();
        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
    }

    @Override
    public void onClick(View v) {
        if(v == accountContinue) {
            createAccount();
        }
    }
}
