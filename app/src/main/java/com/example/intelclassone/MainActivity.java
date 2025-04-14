package com.example.intelclassone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageView profileIcon;
    TextView greetingText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main_3);
        LinearLayout mathsCard = findViewById(R.id.mathsCard);
        LinearLayout physicsCard = findViewById(R.id.physicsCard);
        LinearLayout chemistryCard = findViewById(R.id.chemistryCard);
        LinearLayout biologyCard = findViewById(R.id.biologyCard);
        profileIcon = findViewById(R.id.profile_icon);
        greetingText=findViewById(R.id.greeting_text);


        // Set click listeners
        setCardClickListener(mathsCard, "Maths","#efa139","#ffffff","math");
        setCardClickListener(physicsCard, "Physics","#eb736f","#ffffff","physics");
        setCardClickListener(chemistryCard, "Chemistry","#3276cb","#ffffff","chem");
        setCardClickListener(biologyCard, "Biology","#699e2f","#ffffff","bio");


        CardView cardView = findViewById(R.id.daily_class_card); // Replace with actual ID
        Animation oscillate = AnimationUtils.loadAnimation(this, R.anim.oscillate);
        cardView.startAnimation(oscillate);

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                startActivity(intent);
            }
        });


        greetingText.post(() -> {
            Paint paint = greetingText.getPaint();
            float width = paint.measureText(greetingText.getText().toString());
            Shader shader = new LinearGradient(
                    0, 0, width, greetingText.getTextSize(),
                    new int[]{Color.parseColor("#466cff"), Color.parseColor("#fb0d05")},
                    null,
                    Shader.TileMode.CLAMP
            );
            greetingText.getPaint().setShader(shader);
            greetingText.invalidate();
        });



    }

    private void setCardClickListener(LinearLayout card, final String subject,String bgColor,String textColor,String ImageId) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MultiViewActivity.class);
                intent.putExtra("subject_name", subject);
                intent.putExtra("bgColor", bgColor);
                intent.putExtra("textColor", textColor);
                intent.putExtra("imageId", ImageId);

                startActivity(intent);
            }
        });



    }
}