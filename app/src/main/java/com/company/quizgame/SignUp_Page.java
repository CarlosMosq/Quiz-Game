package com.company.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUp_Page extends AppCompatActivity {

    TextView emailField;
    TextView passwordField;
    Button createAccount;
    TextView existingAccount;
    String email;
    String password;
    ProgressBar progressBar;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sign Up");
        setContentView(R.layout.activity_sign_up_page);

        emailField = findViewById(R.id.newEmail);
        passwordField = findViewById(R.id.newPassword);
        createAccount = findViewById(R.id.createAccount);
        existingAccount = findViewById(R.id.existingAccount);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        createAccount.setOnClickListener(v -> {
            createAccount.setClickable(false);
            email = emailField.getText().toString();
            password = passwordField.getText().toString();
            signUpWithFirebase(email, password);
        });

        existingAccount.setOnClickListener(v -> goToLoginPage());

    }


    public void goToLoginPage() {
        Intent intent = new Intent(SignUp_Page.this, Login_Page.class);
        startActivity(intent);
        finish();
    }

    public void signUpWithFirebase(String userEmail, String userPassword) {
        progressBar.setVisibility(View.VISIBLE);

        auth
                .createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp_Page.this
                                , "Account Created Successfully"
                                , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SignUp_Page.this
                                , "A problem occurred"
                                , Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

}