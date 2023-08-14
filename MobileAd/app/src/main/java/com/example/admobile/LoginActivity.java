package com.example.admobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void RedirectToRegister(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void RedirectToHome(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    public void Login(View v) {
        TextView nameTextView = findViewById(R.id.NameTextViewLogin);
        TextView passwordTextView = findViewById(R.id.PasswordTextViewLogin);

        String name = nameTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        TokenManager tokenManager = new TokenManager(this);

        String endpoint = "businesses/login";
        NetworkUtils.sendGetRequestWithBasicAuth(endpoint, name, password, new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                String token = response.substring(1, response.length() - 1);

                Log.d("TAG", "Token: " + token);

                tokenManager.saveAccessToken(token);
                RedirectToHome(v);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "Error: " + errorMessage);
            }
        });
    }
}