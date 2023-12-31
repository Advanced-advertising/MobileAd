package com.example.admobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.admobile.utils.NetworkUtils;
import com.example.admobile.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAccessToken();

        if (token != null && !token.equals("")) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    public void SetUkrLang(View v) {
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(new Locale("uk"));
        res.updateConfiguration(config, res.getDisplayMetrics());
        recreate();
    }

    public void SetEngLang(View v) {
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(new Locale("en"));
        res.updateConfiguration(config, res.getDisplayMetrics());
        recreate();
    }

    public void RedirectToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    public void Register(View v) {
        TextView nameTextView = findViewById(R.id.NameTextView);
        TextView emailTextView = findViewById(R.id.EmailTextView);
        TextView phoneNumberTextView = findViewById(R.id.PhoneNumberTextView);
        TextView passwordTextView = findViewById(R.id.PasswordTextView);
        TextView repeatPasswordTextView = findViewById(R.id.RepeatPasswordTextView);

        String name = nameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String phoneNumber = phoneNumberTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        String repeatPassword = repeatPasswordTextView.getText().toString();

        if (!password.equals(repeatPassword)) {
            passwordTextView.setText("");
            repeatPasswordTextView.setText("");

            passwordTextView.setHint("Passwords must match");
            passwordTextView.setHintTextColor(Color.RED);
            return;
        }

        String endpoint = "businesses/register";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("business_name", name);
            jsonBody.put("email", email);
            jsonBody.put("phone_number", phoneNumber);
            jsonBody.put("password", password);

            String jsonData = jsonBody.toString();

            NetworkUtils.sendPostRequestAsync(endpoint, jsonData, new NetworkUtils.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    RedirectToLogin(v);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.d("TAG", "Error: " + errorMessage);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}