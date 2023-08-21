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

import com.example.admobile.R;
import com.example.admobile.models.Income;

import java.util.ArrayList;

public class IncomeAdapter extends ArrayAdapter<Income> {
    private Context context;
    private ArrayList<Income> incomes;

    public IncomeAdapter(Context context, ArrayList<Income> incomes) {
        super(context, R.layout.screen_item, incomes);
        this.context = context;
        this.incomes = incomes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.income_item, parent, false);
        }

        Income income = incomes.get(position);

        TextView clientNameTextView = view.findViewById(R.id.income_client_name);
        TextView adNameTextView = view.findViewById(R.id.ad_income_name);

        adNameTextView.setText(income.ad.ad_name);
        clientNameTextView.setHint(income.client.user_name);

        Button detailsButton = view.findViewById(R.id.income_price_button);
        detailsButton.setText("Income: " + income.price + "$");

        return view;
    }
}
