package com.example.admobile.utils;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.admobile.R;
import com.example.admobile.models.Category;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Context context;
    private ArrayList<Category> categories;
    private ArrayList<Category> selectedCategories;

    public CategoryAdapter(Context context, ArrayList<Category> categories, ArrayList<Category> selectedCategories) {
        super(context, R.layout.category_item, categories);
        this.context = context;
        this.categories = categories;
        this.selectedCategories = selectedCategories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.category_item, parent, false);
        }

        Category category = categories.get(position);
        CheckBox checkBox = view.findViewById(R.id.category_item);
        checkBox.setText(category.category_name);

        boolean isChecked = false;
        for (Category selectedCat : selectedCategories) {
            if (Objects.equals(selectedCat.category_id, category.category_id)) {
                isChecked = true;
            }
        }
        checkBox.setChecked(isChecked);

        return view;
    }
}
