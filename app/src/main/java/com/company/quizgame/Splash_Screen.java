package com.company.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    ImageView imageSplash;
    TextView titleSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageSplash = findViewById(R.id.imageViewSplash);
        titleSplash = findViewById(R.id.textViewSplash);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);

        titleSplash.startAnimation(animation);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash_Screen.this, Login_Page.class);
            startActivity(intent);
            finish();
        }, 5000);
    }
}