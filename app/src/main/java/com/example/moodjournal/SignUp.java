package com.example.moodjournal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button signupButton;
    private TextView loginText;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToWelcomePage();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        signupButton = findViewById(R.id.signup_button);
        loginText = findViewById(R.id.login_text);
        mAuth = FirebaseAuth.getInstance();

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    // Show error message to user
                    Toast.makeText(SignUp.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {

//                    mAuth.createUserWithEmailAndPassword(email, password)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign up success, update UI with the signed-in user's information
//                                        Toast.makeText(SignUp.this, "Account Created.", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(getApplicationContext(), Login.class);
//                                        startActivity(intent);
//                                    } else {
//                                        // If sign up fails, display a message to the user.
//                                        Toast.makeText(SignUp.this, "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getApplicationContext(),
                                                        "registration successful!",
                                                        Toast.LENGTH_LONG)
                                                .show();
                                        login();

                                    } else {
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(SignUp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });


                }

                }

        });
    }

    private void registerNewUser(){
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
    }


    private void login() {
        startActivity(new Intent(this, Login.class));
        finish();
    }

    private void goToWelcomePage() {
        startActivity(new Intent(SignUp.this, WelcomePage.class));
        finish();
    }

    private boolean isValidEmail(String username) {
        return username.matches("[a-zA-Z0-9]+");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.length() <= 16 && password.matches("^(?=.*[a-zA-Z0-9])(?=.*[@#$%^&+=]).+$");
    }

    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
