package com.example.intelclassone;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class videoActivity extends AppCompatActivity {

    private ExoPlayer player;
    private PlayerView playerView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    TextView testText;

    private static final String TAG = "videoActivity";
    private static final String VIDEO_URL = "https://dklc7mpgvbwz0.cloudfront.net/videoplayback.mp4"; // Your CDN video link

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);

        // Link to layout playerView
        playerView = findViewById(R.id.playerView);
        testText= findViewById(R.id.test);

        // Firebase Initialization
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Debug log
        Log.d(TAG, "onCreate: Firebase initialized");



        Button btnRewind = findViewById(R.id.btnRewind);
        Button btnPlayPause = findViewById(R.id.btnPlayPause);
        Button btnForward = findViewById(R.id.btnForward);

        btnPlayPause.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                btnPlayPause.setText("▶️");
            } else {
                player.play();
                btnPlayPause.setText("⏸️");
            }
        });

        btnForward.setOnClickListener(v -> {
            long position = player.getCurrentPosition();
            long duration = player.getDuration();
            long seekForwardMs = 10000; // 10 seconds

            long newPosition = Math.min(position + seekForwardMs, duration);
            player.seekTo(newPosition);
        });

        btnRewind.setOnClickListener(v -> {
            long position = player.getCurrentPosition();
            long seekBackMs = 10000; // 10 seconds

            long newPosition = Math.max(position - seekBackMs, 0);
            player.seekTo(newPosition);
        });


        // Fetch from Firestore
        fetchQuizData();

        // Initialize ExoPlayer
        initializePlayer();
       // fetchDataFromFirestore();
    }

    private void fetchQuizData() {
        db.collection("quizz")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        QuerySnapshot querySnapshot = task.getResult();

                        testText.setText("success");

                        StringBuilder resultBuilder = new StringBuilder();

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String docId = document.getId();



                            Map<String, Object> data = document.getData();
                            resultBuilder.append("Document ID: ").append(docId).append("\n");
                            resultBuilder.append("Data: ").append(data.toString()).append("\n\n");
                        }

                        String resultString = resultBuilder.toString();
                        



//                        for (QueryDocumentSnapshot document : querySnapshot) {
//                            String docId = document.getId();
//                            Log.d(TAG, "Quiz document ID: " + docId);
//
//                        }
                    } else {

                        Log.e(TAG, "Firestore error", task.getException());
                        Toast.makeText(videoActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("quizz").document("ab").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> quizData = (List<Map<String, Object>>) documentSnapshot.get("mapsData");

                        testText.setText("success");

                        if (quizData != null && !quizData.isEmpty()) {
                            Map<String, Object> workingData = quizData.get(0); // Get first item

                            // Convert Map to String for display
                            StringBuilder dataString = new StringBuilder();
                            for (Map.Entry<String, Object> entry : workingData.entrySet()) {
                                dataString.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                            }

//                            testText.setText(dataString.toString());

                            // Do something with the first map

                        } else {
                            Log.e("Firestore", "mapsData is empty or null");
                            testText.setText("success 2");

                        }
                    } else {
                        Log.e("Firestore", "Document does not exist");
                        testText.setText("success 3");

                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching document", e);
                    testText.setText(e.getMessage());

                });
    }



    private void initializePlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(VIDEO_URL));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
