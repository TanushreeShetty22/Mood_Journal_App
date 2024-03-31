package com.example.moodjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodjournal.adapters.NewsAdapter;
import com.example.moodjournal.models.NewsArticle;
import com.example.moodjournal.models.NewsResponse;
import com.example.moodjournal.utils.NewsAPI;
import com.example.moodjournal.utils.NewsAPIService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InsightsPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights_page);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter();
        recyclerView.setAdapter(newsAdapter);
        ImageView customMenuIcon = findViewById(R.id.custom_menu_icon);

        customMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        // Set up the bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Fetch mental health-related news articles
        fetchMentalHealthNews();
    }

    private void fetchMentalHealthNews() {
        Retrofit retrofit = NewsAPI.getRetrofitInstance();
        NewsAPIService service = retrofit.create(NewsAPIService.class);
        Call<NewsResponse> call = service.getNews("mental health", getString(R.string.ApiKey));

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewsResponse newsResponse = response.body();
                    List<NewsArticle> newsArticles = newsResponse.getArticles();
                    newsAdapter.setNewsArticles(newsArticles);
                } else {
                    Toast.makeText(InsightsPage.this, "Failed to fetch news articles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Toast.makeText(InsightsPage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setGravity(Gravity.END); // Align the popup to the right side of the custom icon
        popup.inflate(R.menu.account_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_profile) {
                startActivity(new Intent(InsightsPage.this, Profile.class));
                return true;
            } else if (item.getItemId() == R.id.action_signout) {
                startActivity(new Intent(InsightsPage.this, Login.class));
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
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(InsightsPage.this, HomePage.class));
                    return true;
                } else if (itemId == R.id.nav_history) {
                    startActivity(new Intent(InsightsPage.this, MoodHistoryPage.class));
                    return true;
                } else if (itemId == R.id.nav_insights) {
                    startActivity(new Intent(InsightsPage.this, InsightsPage.class));
                    return true;
                } else if (itemId == R.id.nav_settings) {
                    startActivity(new Intent(InsightsPage.this, SettingPage.class));
                    return true;
                }
                return false;
            };
}
