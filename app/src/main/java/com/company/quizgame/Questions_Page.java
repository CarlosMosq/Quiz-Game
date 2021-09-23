package com.company.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Questions_Page extends AppCompatActivity {

    TextView timeCount;
    TextView correctCount;
    TextView wrongCount;
    TextView questionMain;
    TextView answerOne;
    TextView answerTwo;
    TextView answerThree;
    TextView answerFour;
    Button finishGame;
    Button ok;

    String quizQuestion, a, b, c, d, answer;
    int questionCount;
    int questionNbr = 1;

    int correctQuestions = 0;
    int wrongQuestions = 0;

    boolean isClicked = false;

    CountDownTimer timer;
    int timeLeft = 25000;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Questions");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference authRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_page);

        timeCount = findViewById(R.id.timeCount);
        correctCount = findViewById(R.id.correctCount);
        wrongCount = findViewById(R.id.wrongCount);
        questionMain = findViewById(R.id.mainQuestion);
        answerOne = findViewById(R.id.answer1);
        answerTwo = findViewById(R.id.answer2);
        answerThree = findViewById(R.id.answer3);
        answerFour = findViewById(R.id.answer4);
        finishGame = findViewById(R.id.finishButton);
        ok = findViewById(R.id.okButton);

        correctCount.setText(String.valueOf(correctQuestions));
        wrongCount.setText(String.valueOf(wrongQuestions));

        game();
        countDown();

        finishGame.setOnClickListener(v -> {
            sendScore();
            gameConclusion();
        });

        ok.setOnClickListener(v -> {
            game();
            restartCountdown();
        });

        answerOne.setOnClickListener(v -> checkAnswer(answerOne));
        answerTwo.setOnClickListener(v -> checkAnswer(answerTwo));
        answerThree.setOnClickListener(v -> checkAnswer(answerThree));
        answerFour.setOnClickListener(v -> checkAnswer(answerFour));


    }


    public void game() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionCount = (int) dataSnapshot.getChildrenCount();

                quizQuestion = returnString(dataSnapshot, questionNbr, "q");
                a = returnString(dataSnapshot, questionNbr, "a");
                b = returnString(dataSnapshot, questionNbr, "b");
                c = returnString(dataSnapshot, questionNbr, "c");
                d = returnString(dataSnapshot, questionNbr, "d");
                answer = returnString(dataSnapshot, questionNbr, "answer");

                questionMain.setText(quizQuestion);
                resetAnswer(answerOne, a);
                resetAnswer(answerTwo, b);
                resetAnswer(answerThree, c);
                resetAnswer(answerFour, d);
                isClicked = false;

                if (questionNbr < questionCount) {
                    questionNbr++;
                }
                else if(correctQuestions + wrongQuestions == questionCount) {
                    gameConclusion();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast
                        .makeText(Questions_Page.this
                                , "Sorry, a problem occurred"
                                , Toast.LENGTH_SHORT)
                        .show();

            }
        });
    }

    public String returnString(DataSnapshot dataSnapshot, int item, String path) {
        return dataSnapshot.child(String.valueOf(item)).child(path).getValue().toString();
    }

    public void resetAnswer(TextView answer, String text) {
        answer.setText(text);
        answer.setBackgroundColor(Color.WHITE);
        answer.setClickable(true);
    }

    public void checkAnswer(TextView option) {
        if (correctQuestions + wrongQuestions == questionCount && correctQuestions > wrongQuestions) {
            Toast.makeText(Questions_Page.this, "You won!", Toast.LENGTH_LONG).show();
            gameConclusion();
        }
        else if(correctQuestions + wrongQuestions == questionCount && correctQuestions <= wrongQuestions) {
            Toast.makeText(Questions_Page.this, "You lose!", Toast.LENGTH_LONG).show();
            gameConclusion();
        }
        else if (option.getText().toString().equals(answer) && !isClicked){
            option.setBackgroundColor(Color.GREEN);
            correctQuestions++;
            correctCount.setText(String.valueOf(correctQuestions));
            pauseCountDown();
        }
        else if (!option.getText().toString().equals(answer) && !isClicked){
            option.setBackgroundColor(Color.RED);
            wrongQuestions++;
            wrongCount.setText(String.valueOf(wrongQuestions));
            pauseCountDown();
            findCorrect(answerOne, answerTwo, answerThree, answerFour);
        }
        else {
            Toast.makeText(Questions_Page.this, "Option already chosen", Toast.LENGTH_SHORT).show();
        }
        isClicked = true;
        option.setClickable(false);
    }

    public void gameConclusion() {
        Intent overIntent = new Intent(Questions_Page.this, Game_Over.class);
        overIntent.putExtra("score", correctQuestions);
        overIntent.putExtra("total", questionCount);
        startActivity(overIntent);
        finish();
    }

    public void countDown() {
        timer = new CountDownTimer(timeLeft, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeCount.setText(String.valueOf((int) millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (questionNbr < questionCount) {
                    game();
                    wrongQuestions++;
                }
                else if(questionNbr == questionCount) {
                    gameConclusion();
                }
            }
        }.start();
    }

    public void restartCountdown() {
        timer.cancel();
        countDown();
    }

    public void pauseCountDown() {
        timer.cancel();
    }

    public void sendScore() {
        String userUID = user.getUid();
        authRef.child("scores").child(userUID).child("correct").setValue(correctQuestions)
                .addOnSuccessListener(v -> Toast.makeText(Questions_Page.this
                        , "Correct answers submitted"
                        , Toast.LENGTH_SHORT).show());
        authRef.child("scores").child(userUID).child("wrong").setValue(correctQuestions)
                .addOnSuccessListener(v -> Toast.makeText(Questions_Page.this
                        , "Wrong answers submitted"
                        , Toast.LENGTH_SHORT).show());
    }

    public void checkIfCorrect(TextView item) {
        if (item.getText().toString().equals(answer)) {
            item.setBackgroundColor(Color.GREEN);
        }
    }

    public void findCorrect(TextView itemA, TextView itemB, TextView itemC, TextView itemD) {
        checkIfCorrect(itemA);
        checkIfCorrect(itemB);
        checkIfCorrect(itemC);
        checkIfCorrect(itemD);
    }

}