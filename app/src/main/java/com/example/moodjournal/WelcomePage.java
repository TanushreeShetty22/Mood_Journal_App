package com.example.moodjournal;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_page);
        TextView textView = findViewById(R.id.welcome);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        fadeIn.setDuration(5000); // 2 seconds
        fadeIn.start();
        // Direct access to daily mood tracking
        Button dailyMoodButton = findViewById(R.id.buttonDailyMood);
        dailyMoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomePage.this, HomePage.class));
            }
        });
    }
}
