package com.example.moodjournal;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Find the TextView in the layout
        textViewEmail = findViewById(R.id.textViewEmail);

        // Get current signed-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in, get the email
            String userEmail = currentUser.getEmail();
            // Set the email to the TextView
            textViewEmail.setText(userEmail);
        } else {
            // No user is signed in, handle it accordingly
            textViewEmail.setText("No user signed in");
        }
    }
}
