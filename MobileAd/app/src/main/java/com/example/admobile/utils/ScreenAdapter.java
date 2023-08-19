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
import com.example.admobile.models.Screen;

import java.util.ArrayList;

public class ScreenAdapter extends ArrayAdapter<Screen> {
    private Context context;
    private ArrayList<Screen> screens;

    public ScreenAdapter(Context context, ArrayList<Screen> screens) {
        super(context, R.layout.screen_item, screens);
        this.context = context;
        this.screens = screens;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.screen_item, parent, false);
        }

        Screen screen = screens.get(position);

        TextView screenNameTextView = view.findViewById(R.id.screen_name);
        Button detailsButton = view.findViewById(R.id.details_screen_button);

        screenNameTextView.setText(screen.screen_name);
        detailsButton.setText(R.string.details_text);

        detailsButton.setOnClickListener(v -> {

        });

        return view;
    }
}
