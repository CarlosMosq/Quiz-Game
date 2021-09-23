package com.company.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Game_Over extends AppCompatActivity {

    TextView scoreDisplay;
    TextView result;
    Button restart;
    int score;
    int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        scoreDisplay = findViewById(R.id.scoreView);
        result = findViewById(R.id.result);
        restart = findViewById(R.id.restartButton);

        Intent data = getIntent();
        score = data.getIntExtra("score", 0);
        totalQuestions = data.getIntExtra("total", 0);

        scoreDisplay.setText(String.format("%s / %s", String.valueOf(score), String.valueOf(totalQuestions)));

        if ((double) score / (double) totalQuestions > 0.5) {
            result.setText("You Won!");
        }
        else {
            result.setText("You lose!");
        }

        restart.setOnClickListener(v -> {
            Intent restartIntent = new Intent(Game_Over.this, MainActivity.class);
            startActivity(restartIntent);
            finish();
        });

    }
}