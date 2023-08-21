package com.example.admobile.fragments;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admobile.models.BusinessInfo;
import com.example.admobile.utils.CategoryAdapter;
import com.example.admobile.utils.NetworkUtils;
import com.example.admobile.R;
import com.example.admobile.utils.TokenManager;
import com.example.admobile.models.Category;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {
    Context _context;
    private ListView listView;
    private Map<String, String> mapCategories = new HashMap<>();
    private FragmentInteractionListener interactionListener;
    private View rootView;
    private BusinessInfo businessInfo;
    private Category[] categories;
    private CategoryAdapter adapter;

    public ProfileFragment(Context context) {
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        listView = rootView.findViewById(R.id.categoryListView);

        LoadInfo();

        Button saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            Save();
        });

        Button logoutButton = rootView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> interactionListener.onLogout());

        ImageButton engButton = rootView.findViewById(R.id.engButtonProfile);
        engButton.setOnClickListener(v -> {
            SetEngLang();
        });

        ImageButton ukrButton = rootView.findViewById(R.id.ukrButtonProfile);
        ukrButton.setOnClickListener(v -> {
            SetUkrLang();
        });

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

    public void LoadInfo() {
        String endpoint = "businesses/get_business_info";

        TokenManager tokenManager = new TokenManager(_context);
        String token = tokenManager.getAccessToken();

        NetworkUtils.sendPostRequestWithToken(endpoint, token, "", new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                businessInfo = gson.fromJson(response, BusinessInfo.class);

                LoadCategories(businessInfo.categories);

                requireActivity().runOnUiThread(() -> {
                    EditText nameEditText = rootView.findViewById(R.id.editName);
                    EditText emailEditText = rootView.findViewById(R.id.editEmail);
                    EditText phoneNumberEditText = rootView.findViewById(R.id.editPhoneNumber);

                    nameEditText.setText(businessInfo.business_name);
                    emailEditText.setText(businessInfo.email);
                    phoneNumberEditText.setText(businessInfo.phone_number);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "Error: " + errorMessage);
            }
        });
    }

    public void LoadCategories(ArrayList<Category> businessCategories) {
        String endpoint = "categories/get_all";

        TokenManager tokenManager = new TokenManager(_context);
        String token = tokenManager.getAccessToken();

        NetworkUtils.sendGetRequestWithTokenAndJson(endpoint, token, "", new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                categories = gson.fromJson(response, Category[].class);

                requireActivity().runOnUiThread(() -> {
                    adapter = new CategoryAdapter(requireActivity(), new ArrayList<>(Arrays.asList(categories)), businessCategories);
                    listView.setAdapter(adapter);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "Error: " + errorMessage);
            }
        });
    }

    public void SetUkrLang() {
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(new Locale("uk"));
        res.updateConfiguration(config, res.getDisplayMetrics());
        recreate();
    }

    public void SetEngLang() {
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(new Locale("en"));
        res.updateConfiguration(config, res.getDisplayMetrics());
        recreate();
    }

    private void recreate() {
        interactionListener.onRefreshRequested();
    }

    public void Save() {
        EditText nameEditText = rootView.findViewById(R.id.editName);
        EditText emailEditText = rootView.findViewById(R.id.editEmail);
        EditText phoneNumberEditText = rootView.findViewById(R.id.editPhoneNumber);

        businessInfo.business_name = nameEditText.getText().toString();
        businessInfo.email = emailEditText.getText().toString();
        businessInfo.phone_number = phoneNumberEditText.getText().toString();
        businessInfo.screens = new ArrayList<>();

        ArrayList<Category> selectedCategories = new ArrayList<>();

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItemView = listView.getChildAt(i);
            if (listItemView == null) {
                break;
            }
            CheckBox checkBox = listItemView.findViewById(R.id.category_item);

            if (checkBox.isChecked()) {
                Category category = adapter.getItem(i);
                selectedCategories.add(category);
            }
        }

        businessInfo.categories = selectedCategories;

        SendSaveRequest(businessInfo);
    }

    private void SendSaveRequest(BusinessInfo businessInfo) {
        String endpoint = "businesses/change_business_info";

        TokenManager tokenManager = new TokenManager(_context);
        String token = tokenManager.getAccessToken();

        Gson gson = new Gson();
        String json = gson.toJson(businessInfo);

        NetworkUtils.sendPostRequestWithToken(endpoint, token, json, new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(rootView.getContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "Error: " + errorMessage);
            }
        });
    }
}