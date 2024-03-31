package com.example.moodjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.PopupMenu;
import com.google.firebase.auth.FirebaseAuth;

public class app_bar_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ImageView customMenuIcon = findViewById(R.id.custom_menu_icon);

        customMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.account_menu); // Inflate your menu resource here
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_profile) {
                    startActivity(new Intent(app_bar_layout.this, Profile.class));
                    return true;
                } else if (item.getItemId() == R.id.action_signout) {
                    // Handle signout action
                    FirebaseAuth.getInstance().signOut();
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }
}
