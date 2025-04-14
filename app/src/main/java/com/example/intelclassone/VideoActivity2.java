package com.example.intelclassone;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity2 extends AppCompatActivity {

    private ExoPlayer player;
    private PlayerView playerView;
    private ProgressBar loadingSpinner;

    private static final String VIDEO_URL = "https://dklc7mpgvbwz0.cloudfront.net/videoplayback.mp4";
    private static final String PREFS_NAME = "VideoPrefs";
    private static final String VIDEO_POSITION_KEY = "video_position";

    private Handler positionHandler;
    private Runnable positionRunnable;
    private boolean jumpedTo15 = false;
    private boolean jumpedTo55 = false;

    private long playbackPosition = 0L;
    private boolean resumeHandled = false;
    long startPosition , endPosition;

    private List<TimeRange> timeRanges;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playerView = findViewById(R.id.playerView);
        loadingSpinner = findViewById(R.id.loadingSpinner);

        playbackPosition = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getLong(VIDEO_POSITION_KEY, 0);

    }

    private void initializePlayer() {
        if (player == null) {
            player = new ExoPlayer.Builder(this).build();

            player.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                            .setUsage(C.USAGE_MEDIA)
                            .build(),
                    true
            );

            playerView.setPlayer(player);

            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(VIDEO_URL));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);

            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == Player.STATE_BUFFERING) {
                        loadingSpinner.setVisibility(View.VISIBLE);
                    } else if (state == Player.STATE_READY) {
                        loadingSpinner.setVisibility(View.GONE);

                        if (!resumeHandled) {
                            player.seekTo(playbackPosition);
                            player.play();
                            resumeHandled = true;
                            startPositionMonitoring();
                        }
                    }
                }
            });
        }
    }


    private void startPositionMonitoring() {
        positionHandler = new Handler(Looper.getMainLooper());

        positionRunnable = new Runnable() {
            @Override
            public void run() {
                if (player != null && player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady()) {
                    long currentPos = player.getCurrentPosition();


                    if (isInTimeRange(currentPos)) {
                        Toast.makeText(VideoActivity2.this, "is in range", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VideoActivity2.this, "Not in any range.", Toast.LENGTH_SHORT).show();
                    }


                    if (currentPos < 30000 && !jumpedTo15) {
                        player.seekTo(15000);
                        jumpedTo15 = true;
                    }

                    if (currentPos > 40000 && currentPos < 55000 && !jumpedTo55) {
                        player.seekTo(55000);
                     //   jumpedTo55 = true;
                    }

                    if (currentPos >= 30000 && currentPos <= 40000) {
                        jumpedTo15 = false;
                    }
                }

                positionHandler.postDelayed(this, 500);
            }
        };

        positionHandler.post(positionRunnable);
    }

    private void stopPositionMonitoring() {
        if (positionHandler != null && positionRunnable != null) {
            positionHandler.removeCallbacks(positionRunnable);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            stopPositionMonitoring();
            playbackPosition = player.getCurrentPosition();
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putLong(VIDEO_POSITION_KEY, playbackPosition)
                    .apply();

            player.release();
            player = null;
            resumeHandled = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (android.os.Build.VERSION.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT <= 23 || player == null) {
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            long currentPos = player.getCurrentPosition();
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putLong(VIDEO_POSITION_KEY, currentPos)
                    .apply();
        }

        if (android.os.Build.VERSION.SDK_INT <= 23) {
            releasePlayer();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (android.os.Build.VERSION.SDK_INT > 23) {
            releasePlayer();
        }
    }


    public static class TimeRange {
        public long start;
        public long end;

        public TimeRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
    private void setupTimeRanges() {
        timeRanges = new ArrayList<>();
        timeRanges.add(new TimeRange(0, 15000));
        timeRanges.add(new TimeRange(40000, 55000));
        timeRanges.add(new TimeRange(70000, 90000));
    }

    // ✅ Check if current time is in any range
    private boolean isInTimeRange(long currentTime) {
        for (TimeRange range : timeRanges) {
            if (currentTime >= range.start && currentTime <= range.end) {
                return true;
            }
        }
        return false;
    }
}

