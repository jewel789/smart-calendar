package com.example.smartcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtMail;
    private EditText txtPass;
    private EditText txtMailAgain;
    private EditText txtPassAgain;
    private Button signUp;
    private TextView signIn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            Toast.makeText(getApplicationContext(), "Welcome " + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
        }

        txtMail = findViewById(R.id.signUpEmail);
        txtPass = findViewById(R.id.signUpPassword);
        txtMailAgain = findViewById(R.id.signUpEmailAgain);
        txtPassAgain = findViewById(R.id.signUpPasswordAgain);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signInInstead);

        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }

    private void registerAccount() {
        final String email = txtMail.getText().toString().trim();
        final String password = txtPass.getText().toString().trim();
        final String emailAgain = txtMailAgain.getText().toString().trim();
        final String passwordAgain = txtPassAgain.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(emailAgain)) {
            Toast.makeText(this, "Please repeat your email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!email.equals(emailAgain)){
            Toast.makeText(this, "Emails do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(passwordAgain)) {
            Toast.makeText(this, "Please repeat your password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!password.equals(passwordAgain)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Account Registered", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == signUp) {
            registerAccount();
        }
        if(view == signIn) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
