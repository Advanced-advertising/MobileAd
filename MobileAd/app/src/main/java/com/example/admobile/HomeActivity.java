package com.example.admobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

public class HomeActivity extends AppCompatActivity {
    private ImageButton businessButton;
    private ImageButton notificationButton;
    private ImageButton walletButton;
    private ImageButton profileButton;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        businessButton = findViewById(R.id.businessButton);
        notificationButton = findViewById(R.id.notificationButton);
        walletButton = findViewById(R.id.walletButton);
        profileButton = findViewById(R.id.profileButton);

        loadScreenFragment();
        businessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadScreenFragment();
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        walletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void setNewFragment(Fragment newFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, newFragment);
        ft.addToBackStack(null); // Replace fragment
        ft.commit();
    }

    private void loadScreenFragment() {
        ScreensFragment screensFragment = new ScreensFragment();
        setNewFragment(screensFragment);
    }


    public void LoadingCategories(View v) {
        String endpoint = "categories/get_all";

        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAccessToken();
        Log.d("TAG", "Error: " + token);

        NetworkUtils.sendGetRequestWithTokenAndJson(endpoint, token, "", new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("TAG", "categories: " + response);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "Error: " + errorMessage);
            }
        });
    }
}