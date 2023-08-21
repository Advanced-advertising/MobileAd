package com.example.admobile.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.admobile.R;
import com.example.admobile.fragments.AdOrderFragment;
import com.example.admobile.fragments.FragmentScreen;
import com.example.admobile.models.AdOrderData;
import com.example.admobile.models.Screen;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<AdOrderData> {
    private Context context;
    private ArrayList<AdOrderData> adOrderData;

    public NotificationAdapter(Context context, ArrayList<AdOrderData> adOrderData) {
        super(context, R.layout.screen_item, adOrderData);
        this.context = context;
        this.adOrderData = adOrderData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notification_item, parent, false);
        }

        AdOrderData orderData = adOrderData.get(position);

        TextView adNameTextView = view.findViewById(R.id.ad_notification_name);
        TextView clientNameTextView = view.findViewById(R.id.client_name);

        adNameTextView.setText(orderData.ad.ad_name);
        clientNameTextView.setHint(orderData.client.user_name);

        Button detailsButton = view.findViewById(R.id.details_ad_order_button);
        detailsButton.setText(R.string.details_text);

        detailsButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            Fragment newFragment = new AdOrderFragment(orderData, context);
            fragmentManager.beginTransaction()
                    .replace(R.id.framelayout, newFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
