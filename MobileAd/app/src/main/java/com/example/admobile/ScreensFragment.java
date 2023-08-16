package com.example.admobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class ScreensFragment extends Fragment {
    private List<String> stringList = new ArrayList<>();
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screens, container, false);
        listView = rootView.findViewById(R.id.screenListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.screen_item, R.id.screen_name);

        stringList.add("Screen1");
        stringList.add("Screen1");
        stringList.add("Screen1");
        stringList.add("Screen1");
        adapter.addAll(stringList);
        listView.setAdapter(adapter);

        return rootView;
    }
}