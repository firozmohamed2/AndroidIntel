package com.example.intelclassone;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AdaptiveVideoPlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private ExoPlayer player;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private VideoTabAdapter adapter;
    private HashMap<String, List<String>> tabData;
    private static final String VIDEO_URL = "https://dklc7mpgvbwz0.cloudfront.net/videoplayback.mp4"; // Replace with your AWS CDN video link



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adaptive_video_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        playerView = findViewById(R.id.playerView);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabData = new HashMap<>();
        viewPager = findViewById(R.id.viewPager);  // Ensure this ID matches your XML

        if (viewPager == null) {
            throw new NullPointerException("ViewPager2 is null. Check your XML ID.");
        }
        tabData.put("Overview", Arrays.asList("Introduction", "Summary", "Key Points"));
        tabData.put("Chapters", Arrays.asList("Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4"));
        tabData.put("Discussion", Arrays.asList("Question 1", "Question 2", "Doubts", "Concept Clarifications"));

        // ✅ Step 2: Create fragment instances and pass data
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(VideoTabFragment.newInstance(tabData.get("Overview")));
        fragments.add(VideoTabFragment.newInstance(tabData.get("Chapters")));
        fragments.add(VideoTabFragment.newInstance(tabData.get("Discussion")));

        adapter = new VideoTabAdapter(this, fragments);
        viewPager.setAdapter(adapter);

        initializePlayer();

        // ✅ Step 3: Attach TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Overview"); break;
                case 1: tab.setText("Chapters"); break;
                case 2: tab.setText("Discussion"); break;
            }
        }).attach();
    }

    private void initializePlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(VIDEO_URL));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

}
