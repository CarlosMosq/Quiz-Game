package com.company.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Page extends AppCompatActivity {

    TextView emailField;
    TextView passwordField;
    Button signIn;
    SignInButton googleSignIn;
    TextView signUp;
    TextView forgotPassword;
    String emailAddress;
    String password;
    ProgressBar progressBarSignIn;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sign In");
        setContentView(R.layout.activity_login_page);

        emailField = findViewById(R.id.emailAddress);
        passwordField = findViewById(R.id.passwordField);
        signIn = findViewById(R.id.signInButton);
        googleSignIn = findViewById(R.id.googleSignInButton);
        signUp = findViewById(R.id.signUpLink);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressBarSignIn = findViewById(R.id.progressBarSignIn);

        progressBarSignIn.setVisibility(View.INVISIBLE);

        signIn.setOnClickListener(v -> {
            if (emailField.getText().toString().length() > 0
                    && passwordField.getText().toString().length() > 0) {
                emailAddress = emailField.getText().toString();
                password = passwordField.getText().toString();
                signInWithFirebase(emailAddress, password);
            }
            else {
                Toast.makeText(Login_Page.this, "Please enter your E-mail and Password", Toast.LENGTH_SHORT).show();
            }
        });

        googleSignIn.setOnClickListener(v -> {
            //google Sign in not working due to some configuration missing, figure it out;
            signInGoogle();
        });

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(Login_Page.this, SignUp_Page.class);
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(v -> {
            Intent i = new Intent(Login_Page.this, Forgot_Password.class);
            startActivity(i);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //When you first sign up, this is being triggered, figure out how to fix;
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void signInWithFirebase(String userEmail, String userPassword) {
        progressBarSignIn.setVisibility(View.VISIBLE);
        signIn.setClickable(false);
        auth
                .signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast
                                .makeText(Login_Page.this, "Access granted", Toast.LENGTH_SHORT)
                                .show();
                        Intent i = new Intent(Login_Page.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Toast
                                .makeText(Login_Page.this, "E-mail or password Incorrect", Toast.LENGTH_SHORT)
                                .show();
                        emailField.setText("");
                        passwordField.setText("");
                        signIn.setClickable(true);
                    }
                    progressBarSignIn.setVisibility(View.INVISIBLE);
                });
    }

    public void signInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        startSignIn();
    }

    public void startSignIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            firebaseSignInWithGoogle(task);
        }

    }

    public void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(Login_Page.this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(Login_Page.this, "There was a problem. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void firebaseGoogleAccount(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
            }
            else {

            }
        });
    }
}