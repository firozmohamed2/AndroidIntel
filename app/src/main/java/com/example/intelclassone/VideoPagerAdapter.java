package com.example.intelclassone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class VideoPagerAdapter extends FragmentStateAdapter {

    public VideoPagerAdapter(AppCompatActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new VideoTabFragment();
    }

    @Override
    public int getItemCount() {
        return 3; // 3 Tabs
    }
} 
