package com.example.admobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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