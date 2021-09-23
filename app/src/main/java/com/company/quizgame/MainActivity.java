package com.company.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.quickaccesswallet.QuickAccessWalletService;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button signOut;
    Button startQuiz;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOut = findViewById(R.id.signOutButton);
        startQuiz = findViewById(R.id.startQuizButton);
        signOut.setOnClickListener(v -> signOutWithFirebase());
        startQuiz.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Questions_Page.class);
            startActivity(i);
        });
    }

    public void signOutWithFirebase() {
        signOut.setClickable(false);
        auth.signOut();
        Intent i = new Intent(MainActivity.this, Login_Page.class);
        startActivity(i);
        finish();
    }
}