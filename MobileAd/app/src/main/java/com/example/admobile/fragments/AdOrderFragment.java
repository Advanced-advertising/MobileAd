package com.example.admobile.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admobile.R;
import com.example.admobile.models.AdOrderData;
import com.example.admobile.utils.NetworkUtils;
import com.example.admobile.utils.TokenManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdOrderFragment extends Fragment {
    private Context _context;
    private FragmentInteractionListener interactionListener;
    private View rootView;
    private AdOrderData _adOrderData;

    public AdOrderFragment(AdOrderData adOrderData, Context context) {
        _adOrderData = adOrderData;
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ad_order, container, false);

        LoadInfo();

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

    private void LoadInfo() {
        TextView startTimeText = rootView.findViewById(R.id.startTimeText);
        TextView priceText = rootView.findViewById(R.id.priceText);
        TextView addressNameText = rootView.findViewById(R.id.addressNameText);
        TextView statusNameText = rootView.findViewById(R.id.statusNameText);
        TextView adNameText = rootView.findViewById(R.id.adNameText);
        TextView userNameText = rootView.findViewById(R.id.userNameText);
        TextView userPhoneNumberText = rootView.findViewById(R.id.userPhoneNumberTextView);

        Date date = new Date(_adOrderData.start_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(date);

        startTimeText.setHint("Date: " + formattedDate);
        priceText.setHint("Price: " + _adOrderData.price + "$");
        addressNameText.setHint(_adOrderData.address_name);

        adNameText.setHint(_adOrderData.ad.ad_name);
        userNameText.setHint(_adOrderData.client.user_name);
        userPhoneNumberText.setHint(_adOrderData.client.phone_number);

        ImageView imageView = rootView.findViewById(R.id.adImageView);

        Picasso.get()
                .load(_adOrderData.ad.img_url)
                .placeholder(R.drawable.screen)
                .error(R.drawable.screen)
                .into(imageView);

        Button rejectButton = rootView.findViewById(R.id.rejectButton);
        Button approveButton = rootView.findViewById(R.id.approveButton);

        if (_adOrderData.is_rejected) {
            statusNameText.setHint("Status: Not approved");
            approveButton.setOnClickListener(view -> {
                Approve();
            });
        } else {
            statusNameText.setHint("Status: Approved");
            approveButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
        }
    }

    private void Approve() {
        String endpoint = "businesses/approve_ad_order";

        TokenManager tokenManager = new TokenManager(_context);
        String token = tokenManager.getAccessToken();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("order_id", _adOrderData.order_id);
            String jsonData = jsonBody.toString();

            NetworkUtils.sendPostRequestWithToken(endpoint, token, jsonData, new NetworkUtils.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(rootView.getContext(), "Approved successfully", Toast.LENGTH_SHORT).show();
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