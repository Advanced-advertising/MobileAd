package com.example.admobile.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admobile.R;
import com.example.admobile.models.BusinessInfo;
import com.example.admobile.models.Screen;
import com.example.admobile.models.ScreenData;
import com.example.admobile.utils.NetworkUtils;
import com.example.admobile.utils.TokenManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentScreen extends Fragment {
    private Screen _screen;
    private View rootView;
    Context _context;

    public FragmentScreen(Screen screen, Context context) {
        _screen = screen;
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_screen, container, false);
        LoadScreenInfo();

        return rootView;
    }

    private void LoadScreenInfo() {
        TextView screenNameText = rootView.findViewById(R.id.screenNameTextView);
        TextView pricePerTimeText = rootView.findViewById(R.id.pricePerTimeTextView);
        TextView characteristicsText = rootView.findViewById(R.id.characteristicsTextView);
        TextView trafficText = rootView.findViewById(R.id.trafficTextView);

        screenNameText.setHint(_screen.screen_name);
        pricePerTimeText.setHint(_screen.price_per_time + "$");
        characteristicsText.setHint(_screen.characteristics);
        trafficText.setHint(_screen.traffic + "per day");

        String endpoint = "screens/get_screen_data_by_id";

        TokenManager tokenManager = new TokenManager(_context);
        String token = tokenManager.getAccessToken();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("screen_id", _screen.screen_id);
            String jsonData = jsonBody.toString();

            NetworkUtils.sendPostRequestWithToken(endpoint, token, jsonData, new NetworkUtils.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    Gson gson = new Gson();
                    ScreenData screenData = gson.fromJson(response, ScreenData.class);

                    requireActivity().runOnUiThread(() -> {
                        TextView addressText = rootView.findViewById(R.id.addressTextView);
                        addressText.setHint(screenData.address_name);
                    });
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