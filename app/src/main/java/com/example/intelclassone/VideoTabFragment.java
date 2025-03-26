package com.example.intelclassone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VideoTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<String> itemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_tab, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Sample data
        itemList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            itemList.add("Item " + i);
        }

        adapter = new VideoAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
