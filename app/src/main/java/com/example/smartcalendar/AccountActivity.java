package com.example.smartcalendar;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener{

    private Button accountContinue;
    private EditText accName;
    private EditText accAge;

    private FirebaseUser user;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountContinue = findViewById(R.id.accountContinue);
        accName = findViewById(R.id.accountName);
        accAge = findViewById(R.id.accountAge);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference(user.getUid());

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

        Toast.makeText(getApplicationContext(), "Account Information Saved!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v == accountContinue) {
            createAccount();
        }
    }
}
