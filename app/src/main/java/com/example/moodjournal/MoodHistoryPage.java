package com.example.moodjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MoodHistoryPage extends AppCompatActivity {

    private DatabaseReference userMoodEntriesRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history_page);

        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userMoodEntriesRef = database.getReference("users")
                .child(getUid())
                .child("mood_entries");

        CalendarView calendarView = findViewById(R.id.calendarview);
        ImageView customMenuIcon = findViewById(R.id.custom_menu_icon);

        customMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Format the selected date in the same way as it is stored in the Firebase Realtime Database
                String selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);

                // Display a toast with the selected date
                Toast.makeText(MoodHistoryPage.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();

                // Query the Firebase Realtime Database for mood entries created on the selected date
                userMoodEntriesRef.orderByChild("date").equalTo(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Mood entry found for the selected date, display it in a message box
                            String moodEntryKey = null;
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                moodEntryKey = childSnapshot.getKey();
                                break;
                            }

                            // Query the Firebase Realtime Database for the mood entry using its unique key
                            userMoodEntriesRef.child(moodEntryKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Mood entry found, display it in a message box
                                        String moodText = snapshot.child("moodText").getValue(String.class);
                                        showMessage("Mood entry for " + selectedDate, moodText);
                                    } else {
                                        // No mood entry found for the selected date, show a toast message
                                        Toast.makeText(MoodHistoryPage.this, "No data saved for " + selectedDate, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle any errors that may occur during the query
                                    Toast.makeText(MoodHistoryPage.this, "Failed to read data: " +error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any errors that may occur during the query
                        Toast.makeText(MoodHistoryPage.this, "Failed to read data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Set up the bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private String getUid() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return "";
    }

    private void showMessage(String title, String moodText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(moodText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setGravity(Gravity.END); // Align the popup to the right side of the custom icon
        popup.inflate(R.menu.account_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_profile) {
                startActivity(new Intent(MoodHistoryPage.this, Profile.class));
                return true;
            } else if (item.getItemId() == R.id.action_signout) {
                startActivity(new Intent(MoodHistoryPage.this, Login.class));
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
                    startActivity(new Intent(MoodHistoryPage.this, HomePage.class));
                    return true;
                } else if (itemId == R.id.nav_history) {// Navigate to the MoodHistoryPage activity
                    startActivity(new Intent(MoodHistoryPage.this, MoodHistoryPage.class));

                    return true;
                } else if (itemId == R.id.nav_insights) {// Navigate to the InsightsPage activity
                    startActivity(new Intent(MoodHistoryPage.this, InsightsPage.class));
                    return true;
                } else if (itemId == R.id.nav_settings) {// Navigate to the Settings activity
                    startActivity(new Intent(MoodHistoryPage.this, SettingPage.class));
                    return true;
                }
                return false;
            };
}
