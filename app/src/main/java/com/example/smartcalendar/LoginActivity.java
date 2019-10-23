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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private EditText txtEmail;
    private EditText txtPassword;
    private TextView signUp;

    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient gsc;

    private SignInButton googleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, CalendarActivity.class));
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        loginButton = findViewById(R.id.signIn);
        txtEmail = findViewById(R.id.signInEmail);
        txtPassword = findViewById(R.id.signInPassword);
        signUp = findViewById(R.id.signUpInstead);
        googleSignInButton = findViewById(R.id.googleSignIn);

        loginButton.setOnClickListener(this);
        signUp.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Task <GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                fireBaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Welcome " + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Google Sign-In Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void userLogin() {
        final String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Welcome " + email, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == loginButton) {
            userLogin();
        }

        if(view == signUp) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        if(view == googleSignInButton) {
            googleSignIn();
        }
    }
}
