package com.example.admobile.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.admobile.R;
import com.example.admobile.models.Screen;
import com.example.admobile.utils.NetworkUtils;
import com.example.admobile.utils.ScreenAdapter;
import com.example.admobile.utils.TokenManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class ScreensFragment extends Fragment {
    private ListView listView;
    private FragmentInteractionListener interactionListener;
    Context _context;

    public ScreensFragment(Context context) {
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screens, container, false);
        listView = rootView.findViewById(R.id.screenListView);

        LoadScreens();

        ImageButton reloadButton = rootView.findViewById(R.id.reloadButton);
        reloadButton.setOnClickListener(v -> interactionListener.onRefreshRequested());

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

    public void LoadScreens() {
        String endpoint = "screens/get_all_business_screens";

        TokenManager tokenManager = new TokenManager(_context);
        String token = tokenManager.getAccessToken();

        NetworkUtils.sendGetRequestWithTokenAndJson(endpoint, token, "", new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Screen[] screens = gson.fromJson(response, Screen[].class);

                requireActivity().runOnUiThread(() -> {
                    ScreenAdapter adapter = new ScreenAdapter(requireActivity(), new ArrayList<>(Arrays.asList(screens)));
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