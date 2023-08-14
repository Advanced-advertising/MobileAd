package com.example.admobile;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.*;

public class NetworkUtils {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public interface ApiCallback {
        void onSuccess(String response);
        void onFailure(String errorMessage);
    }

    public static void sendPostRequestAsync(final String url, final String jsonData, final ApiCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
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
}
