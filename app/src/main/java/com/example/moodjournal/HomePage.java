package com.example.moodjournal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomePage extends AppCompatActivity {

    private ImageButton emojiRad, emojiHappy, emojiMeh, emojiUnhappy, emojiAnxious, emojiAngry;
    private Button saveButton;

    private EditText textInput;
    private DatabaseReference userMoodEntriesRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        emojiRad = findViewById(R.id.emoji_rad);
        emojiHappy = findViewById(R.id.emoji_happy);
        emojiMeh = findViewById(R.id.emoji_meh);
        emojiUnhappy = findViewById(R.id.emoji_unhappy);
        emojiAnxious = findViewById(R.id.emoji_anxious);
        emojiAngry = findViewById(R.id.emoji_angry);
        saveButton = findViewById(R.id.save_note_button);
        mAuth = FirebaseAuth.getInstance();



        ImageView customMenuIcon = findViewById(R.id.custom_menu_icon);

        customMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });




        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userMoodEntriesRef = database.getReference("users")
                .child(getUid())
                .child("mood_entries");

        // Initialize your EditText
        textInput = findViewById(R.id.note_edittext);

        // Example of writing user input to Firebase Database on a button click
        saveButton.setOnClickListener(view -> writeInputToFirebase());



        // Set click listeners for each emoji ImageButton (if needed)
        emojiRad.setOnClickListener(v -> {
            showNoteInputLayout(true);
            Toast.makeText(HomePage.this, "You have selected rad", Toast.LENGTH_SHORT).show();
        });

        emojiHappy.setOnClickListener(v -> {
            showNoteInputLayout(true);
            Toast.makeText(HomePage.this, "You have selected happy", Toast.LENGTH_SHORT).show();
        });

        emojiMeh.setOnClickListener(v -> {
            showNoteInputLayout(true);
            Toast.makeText(HomePage.this, "You have selected meh", Toast.LENGTH_SHORT).show();
        });

        emojiUnhappy.setOnClickListener(v -> {
            showNoteInputLayout(true);
            Toast.makeText(HomePage.this, "You have selected sad", Toast.LENGTH_SHORT).show();
        });

        emojiAnxious.setOnClickListener(v -> {
            showNoteInputLayout(true);
            Toast.makeText(HomePage.this, "You have selected anxious", Toast.LENGTH_SHORT).show();
        });

        emojiAngry.setOnClickListener(v -> {
            showNoteInputLayout(true);
            Toast.makeText(HomePage.this, "You have selected angry", Toast.LENGTH_SHORT).show();
        });

        // Set up the bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Set the formatted current date
        TextView dateTextFormatted = findViewById(R.id.date_text_formatted);
        String formattedDate = getCurrentDateFormatted();
        dateTextFormatted.setText(formattedDate);
    }

    private void showNoteInputLayout(boolean show) {
        LinearLayout noteInputLayout = findViewById(R.id.note_input_layout);
        noteInputLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void writeInputToFirebase() {
        String userInput = textInput.getText().toString().trim();
        String userId = getUid();

        if (!userInput.isEmpty()) {
            // Generate a unique key for the mood entry
            String entryId = userMoodEntriesRef.push().getKey();

            // Get the current date
            String currentDate = getCurrentDateFormatted();

            // Create a mood entry object
            MoodEntry moodEntry = new MoodEntry(userInput, currentDate);

            // Save mood entry to Firebase Database
            userMoodEntriesRef.child(entryId).setValue(moodEntry);

            // Clear the EditText
            textInput.setText("");

            Toast.makeText(this, "Mood entry saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter your mood", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDateFormatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private String getUid() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            // Handle the case where the user is not signed in
            // You can redirect the user to the sign-in activity or perform other actions
            return null;
        }
    }
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setGravity(Gravity.END); // Align the popup to the right side of the custom icon
        popup.inflate(R.menu.account_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_profile) {
                startActivity(new Intent(HomePage.this, Profile.class));
                return true;
            } else if (item.getItemId() == R.id.action_signout) {
                startActivity(new Intent(HomePage.this, Login.class));
                FirebaseAuth.getInstance().signOut();
                return true;
            }
            return false;
        });
        popup.show();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {// Navigate to the HomeFragment or HomeActivity
                    startActivity(new Intent(HomePage.this, HomePage.class));
                    return true;
                } else if (itemId == R.id.nav_history) {// Navigate to the MoodHistoryPage activity
                    startActivity(new Intent(HomePage.this, MoodHistoryPage.class));

                    return true;
                } else if (itemId == R.id.nav_insights) {// Navigate to the InsightsPage activity
                    startActivity(new Intent(HomePage.this, InsightsPage.class));
                    return true;
                } else if (itemId == R.id.nav_settings) {// Navigate to the Settings activity
                    startActivity(new Intent(HomePage.this, SettingPage.class));
                    return true;
                }
                return false;
            };
}