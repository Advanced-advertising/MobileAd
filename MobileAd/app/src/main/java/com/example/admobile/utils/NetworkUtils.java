package com.example.admobile.utils;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class NetworkUtils {
    private static final String BASEURL = "http://10.0.2.2:4000/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public interface ApiCallback {
        void onSuccess(String response);
        void onFailure(String errorMessage);
    }

    public static void sendPostRequestAsync(final String endpoint, final String jsonData, final ApiCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String url = BASEURL + endpoint;
                RequestBody requestBody = RequestBody.create(jsonData, JSON);
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Unexpected response code: " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.startsWith("Unexpected") || result.startsWith("java.io")) {
                    callback.onFailure(result);
                } else {
                    callback.onSuccess(result);
                }
            }
        }.execute();
    }

    public static void sendGetRequestWithBasicAuth(String endpoint, String username, String password, final ApiCallback callback) {
        String credentials = username + ":" + password;
        String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        String authHeader = "Basic " + base64Credentials;
        String url = BASEURL + endpoint;
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    callback.onSuccess(responseBody);
                } else {
                    callback.onFailure("Unexpected response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }
        });
    }

    public static void sendGetRequestWithTokenAndJson(String endpoint, String token, String jsonParam, final ApiCallback callback) {
        String authHeader = "Bearer " + token;
        String url = BASEURL + endpoint + "?" + jsonParam;


        Log.d("TAG", "authHeader: " + authHeader);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    callback.onSuccess(responseBody);
                } else {
                    callback.onFailure("Unexpected response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }
        });
    }


    public static void sendPostRequestWithToken(String endpoint, String token, String json, final ApiCallback callback) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        String authHeader = "Bearer " + token;
        String url = BASEURL + endpoint;
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", authHeader)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    callback.onSuccess(responseBody);
                } else {
                    callback.onFailure("Unexpected response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }
        });
    }
}
