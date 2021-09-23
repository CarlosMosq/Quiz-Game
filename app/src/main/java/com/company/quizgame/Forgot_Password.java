package com.company.quizgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    TextView emailAddress;
    Button sendResetLink;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userEmail;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailAddress = findViewById(R.id.resetEmail);
        sendResetLink = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBarReset);
        progressBar.setVisibility(View.INVISIBLE);

        sendResetLink.setOnClickListener(v -> {
            sendResetLink.setClickable(false);
            userEmail = emailAddress.getText().toString();
            resetPassword(userEmail);
        });

    }

    public void resetPassword(String userEmail) {
        progressBar.setVisibility(View.VISIBLE);
        auth
                .sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast
                                .makeText(Forgot_Password.this, "Reset Link Sent To Your E-mail", Toast.LENGTH_SHORT)
                                .show();
                        sendResetLink.setClickable(false);
                    }
                    else {
                        Toast
                                .makeText(Forgot_Password.this, "A problem occurred, try again later", Toast.LENGTH_SHORT)
                                .show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                });
    }
}