package com.example.admobile.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admobile.R;
import com.example.admobile.models.AdOrderData;

public class AdOrderFragment extends Fragment {
    public AdOrderFragment(AdOrderData adOrderData, Context context) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad_order, container, false);
    }
}