package com.example.intelclassone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);
        CardView mathsCard = findViewById(R.id.mathsCard);
        CardView physicsCard = findViewById(R.id.physicsCard);
        CardView chemistryCard = findViewById(R.id.chemistryCard);
        CardView biologyCard = findViewById(R.id.biologyCard);

        // Set click listeners
        setCardClickListener(mathsCard, "Maths");
        setCardClickListener(physicsCard, "Physics");
        setCardClickListener(chemistryCard, "Chemistry");
        setCardClickListener(biologyCard, "Biology");
    }

    private void setCardClickListener(CardView card, final String subject) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdaptiveVideoPlayerActivity.class);
                intent.putExtra("subject_name", subject);
                startActivity(intent);
            }
        });
    }
}