package com.example.moodjournal;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView signupText, forgotPasswordText;
    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            goToWelcomePage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);
        forgotPasswordText = findViewById(R.id.forgot_password_text);

        // Initialize the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString();

                if (isValidEmail(email) && isValidPassword(password)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getApplicationContext(),
                                                        "Login successful!",
                                                        Toast.LENGTH_LONG)
                                                .show();
                                        goToWelcomePage();

                                    } else {
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } else {
                    // Show error message to user for invalid input
                    showError("Invalid username or password. Please check the requirements.");
                    // Additionally, show signup text and link
                    showSignupText();
                }
            }
        });


         TextInputLayout passwordTextInputLayout = findViewById(R.id.password_text_input_layout);
        EditText passwordField = findViewById(R.id.password_field);
        ImageButton showPasswordButton = findViewById(R.id.show_password_button);

        showPasswordButton.setOnClickListener(v -> {
            // Toggle password visibility
            if (passwordField.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPasswordButton.setImageResource(R.drawable.baseline_visibility_24);
            } else {
                passwordField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPasswordButton.setImageResource(R.drawable.baseline_visibility_off_24);
            }
            // Move cursor to the end of the text
            passwordField.setSelection(passwordField.length());
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle forgot password
                handleForgotPassword();
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignupText();
            }
        });
    }


    private boolean isValidEmail(String email) {
        // Email should be in a valid email format
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Password should be between 6-16 characters, alphanumeric, and contain at least one special character
        return password.length() >= 6 && password.length() <= 16 && password.matches("^(?=.*[a-zA-Z0-9]).+$");
    }

    private void goToWelcomePage() {
        // Implement the logic to start the main activity or navigate to the home page
        startActivity(new Intent(Login.this, WelcomePage.class));
    }
    private void handleForgotPassword() {
        String email = emailField.getText().toString().trim();

        if (isValidEmail(email)) {
            // Call Firebase method to send password reset email
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Email sent successfully
                                Toast.makeText(Login.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to send email
                                Toast.makeText(Login.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Show error if email is invalid
            Toast.makeText(Login.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(String errorMessage) {
        // Implement the logic to show an error message to the user
        // You can use Toast or any other UI element to display the error message
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void showSignupText() {
        // Implement the logic to show the text message with a signup link
        signupText.setVisibility(View.VISIBLE);
        // Set an OnClickListener for the signup text to navigate to the signup activity
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic to navigate to the signup activity
                startActivity(new Intent(Login.this, SignUp.class));
            }

        }
        );}
}