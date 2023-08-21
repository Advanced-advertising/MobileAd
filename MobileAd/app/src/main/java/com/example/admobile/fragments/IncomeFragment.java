package com.example.admobile.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.admobile.R;
import com.example.admobile.models.Income;
import com.example.admobile.models.Screen;
import com.example.admobile.utils.IncomeAdapter;
import com.example.admobile.utils.NetworkUtils;
import com.example.admobile.utils.ScreenAdapter;
import com.example.admobile.utils.TokenManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class IncomeFragment extends Fragment {
    private Context _context;
    private View rootView;

    private ListView listView;

    public IncomeFragment(Context context) {
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_income, container, false);
        listView = rootView.findViewById(R.id.incomeListView);

        LoadIncome();

        return rootView;
    }

    private void LoadIncome() {
        String endpoint = "businesses/get_all_business_incomes";

        TokenManager tokenManager = new TokenManager(_context);
        String token = tokenManager.getAccessToken();

        NetworkUtils.sendGetRequestWithTokenAndJson(endpoint, token, "", new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Income[] incomes = gson.fromJson(response, Income[].class);

                requireActivity().runOnUiThread(() -> {
                    IncomeAdapter adapter = new IncomeAdapter(requireActivity(), new ArrayList<>(Arrays.asList(incomes)));
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