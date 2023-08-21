package com.example.admobile.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.admobile.R;
import com.example.admobile.models.AdOrderData;
import com.example.admobile.models.Screen;
import com.example.admobile.utils.NetworkUtils;
import com.example.admobile.utils.NotificationAdapter;
import com.example.admobile.utils.ScreenAdapter;
import com.example.admobile.utils.TokenManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class NotificationFragment extends Fragment {
    private Context _context;

    private ListView listView;
    private FragmentInteractionListener interactionListener;
    private View rootView;

    public NotificationFragment(Context context) {
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        listView = rootView.findViewById(R.id.notificationsListView);

        LoadNotifications();

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            interactionListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement FragmentInteractionListener");
        }
    }

    public void LoadNotifications() {
        String endpoint = "businesses/get_business_ad_orders";

        TokenManager tokenManager = new TokenManager(_context);
        String token = tokenManager.getAccessToken();

        NetworkUtils.sendGetRequestWithTokenAndJson(endpoint, token, "", new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                AdOrderData[] screens = gson.fromJson(response, AdOrderData[].class);

                requireActivity().runOnUiThread(() -> {
                    NotificationAdapter adapter = new NotificationAdapter(requireActivity(), new ArrayList<>(Arrays.asList(screens)));
                    listView.setAdapter(adapter);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "Error: " + errorMessage);
            }
        });
    }
}