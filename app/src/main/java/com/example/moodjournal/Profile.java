package com.example.moodjournal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView textViewEmail;
    private TextView textViewUsername; // Add TextView for username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // Set up the bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // Find the TextViews in the layout
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewUsername = findViewById(R.id.textViewUsername); // Initialize TextView for username

        // Get current signed-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in
            String userEmail = currentUser.getEmail();
            // Set email to the email TextView
            textViewEmail.setText(userEmail);

            // Extract username from email
            String[] parts = userEmail.split("@"); // Split email by "@" symbol
            if (parts.length > 0) {
                String username = parts[0]; // Username is the first part before "@"
                // Set username to the username TextView
                textViewUsername.setText(username);
            } else {
                // If email format is incorrect, display "Unknown" as username
                textViewUsername.setText("Unknown");
            }
        } else {
            // No user is signed in, handle it accordingly
            textViewEmail.setText("No user signed in");
            textViewUsername.setText("N/A");
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {// Navigate to the HomeFragment or HomeActivity
                    startActivity(new Intent(Profile.this, HomePage.class));
                    return true;
                } else if (itemId == R.id.nav_history) {// Navigate to the MoodHistoryPage activity
                    startActivity(new Intent(Profile.this, MoodHistoryPage.class));

                    return true;
                } else if (itemId == R.id.nav_insights) {// Navigate to the InsightsPage activity
                    startActivity(new Intent(Profile.this, InsightsPage.class));
                    return true;
                } else if (itemId == R.id.nav_settings) {// Navigate to the Settings activity
                    startActivity(new Intent(Profile.this, SettingPage.class));
                    return true;
                }
                return false;
            };
}
