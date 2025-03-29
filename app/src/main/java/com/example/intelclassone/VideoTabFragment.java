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

    private static final String ARG_DATA_LIST = "data_list";
    private List<String> dataList;

    public static VideoTabFragment newInstance(List<String> data) {
        VideoTabFragment fragment = new VideoTabFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_DATA_LIST, new ArrayList<>(data));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dataList = getArguments().getStringArrayList(ARG_DATA_LIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_tab, container, false);

        // ✅ Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new VideoTabAdapterRecycler(dataList));

        return view;
    }
}
