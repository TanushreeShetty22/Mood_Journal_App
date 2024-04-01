package com.example.moodjournal;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class SettingPage extends AppCompatActivity {


    private static final String SHARED_PREF_NAME = "settings";
    private static final String KEY_THEME = "theme";
    private static final String KEY_NOTIFICATION = "notification";


    private RadioGroup themeGroup;
    private RadioButton themeLight, themeDark;
    private Switch notificationSwitch;
    private Button saveButton;


    private SharedPreferences sharedPreferences;
    private boolean isNotificationEnabled;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "com.example.moodjournal.NOTIFICATION_CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Mood Journal Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Mood Journal notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        // Initialize views
        themeGroup = findViewById(R.id.theme_group);
        themeLight = findViewById(R.id.theme_light);
        themeDark = findViewById(R.id.theme_dark);
        notificationSwitch = findViewById(R.id.notification_switch);
        saveButton = findViewById(R.id.save_button);
        // Set up the bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);


        // Restore saved settings
        loadSettings();


        // Set click listener for Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }


    private void loadSettings() {
        int savedTheme = sharedPreferences.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        isNotificationEnabled = sharedPreferences.getBoolean(KEY_NOTIFICATION, true);


        // Set initial values for theme and notification settings
        themeLight.setChecked(savedTheme == AppCompatDelegate.MODE_NIGHT_NO);
        themeDark.setChecked(savedTheme == AppCompatDelegate.MODE_NIGHT_YES);
        notificationSwitch.setChecked(isNotificationEnabled);
    }


    private void saveSettings() {
        int selectedTheme = themeLight.isChecked() ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;

        // Apply the selected theme
        AppCompatDelegate.setDefaultNightMode(selectedTheme);

        if (notificationSwitch.isChecked()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.img)
                    .setContentTitle("Mood Journal")
                    .setContentText("Do not forget to journal your mood for today!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // Show the notification
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
        // Save settings to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_THEME, selectedTheme);
        editor.putBoolean(KEY_NOTIFICATION, isNotificationEnabled);
        editor.apply();

        // Show a message indicating that settings have been saved
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();

        // Restart the activity to apply the new theme
        recreate();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_home) {
                        // Navigate to the HomeFragment or HomeActivity
                        startActivity(new Intent(SettingPage.this, HomePage.class));
                        return true;
                    } else if (itemId == R.id.nav_history) {
                        // Navigate to the MoodHistoryPage activity
                        startActivity(new Intent(SettingPage.this, MoodHistoryPage.class));
                        return true;
                    } else if (itemId == R.id.nav_insights) {
                        // Navigate to the InsightsPage activity
                        startActivity(new Intent(SettingPage.this, InsightsPage.class));
                        return true;
                    } else if (itemId == R.id.nav_settings) {
                        // Navigate to the Settings activity
                        startActivity(new Intent(SettingPage.this, SettingPage.class));

                        return true;
                    }
                    return false;
                }
            };

}