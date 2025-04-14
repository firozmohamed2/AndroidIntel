package com.example.intelclassone;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ChapterAdapter chapterAdapter;
    private TextView subjectTextView;
    private Map<String, List<String>> subjectChapters;
    Toolbar toolbar;
    ImageView subjectIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subject);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        subjectTextView = findViewById(R.id.subjectTextView);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        subjectIcon = findViewById(R.id.subject_icon);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // hides default title if needed
        }


        // Get subject name from Intent
        String subject = getIntent().getStringExtra("subject_name");
        String bgColor = getIntent().getStringExtra("bgColor");
        String textColor= getIntent().getStringExtra("textColor");
        String imageId = getIntent().getStringExtra("imageId");
           if (subject !=null) {
               subjectTextView.setText(subject);
           }

        if (bgColor != null) {
            try {
                toolbar.setBackgroundColor(Color.parseColor(bgColor));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }


        if (textColor != null) {
            try {
                subjectTextView.setTextColor(Color.parseColor(textColor));
            } catch (IllegalArgumentException e) {
                Log.e("ColorError", "Invalid textColor: " + textColor);
            }
        }

        if (imageId != null) {
            int resID = getResources().getIdentifier(imageId, "drawable", getPackageName());
            if (resID != 0) {
                subjectIcon.setImageResource(resID);
            } else {
                Log.e("ImageError", "Image not found: " + imageId);
            }
        }

        Drawable upArrow = AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back_24);
        if (upArrow != null) {
            upArrow.setTint(Color.WHITE);  // Cleaner than PorterDuff in newer APIs
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }


        // Initialize chapter data
        initChapterData();

        // Get the chapters for the selected subject
        List<String> chapters = subjectChapters.getOrDefault(subject, new ArrayList<>());

        // Set up RecyclerView with Adapter
        chapterAdapter = new ChapterAdapter(chapters);
        recyclerView.setAdapter(chapterAdapter);
    }

    private void initChapterData() {
        subjectChapters = new HashMap<>();

        subjectChapters.put("Maths", Arrays.asList(
                "Algebra",
                "Trigonometry",
                "Calculus",
                "Probability",
                "Coordinate Geometry",
                "Statistics",
                "Vectors",
                "Matrices",
                "Complex Numbers",
                "Linear Programming",
                "Sets and Relations",
                "Determinants",
                "Differential Equations",
                "Binomial Theorem",
                "Mathematical Reasoning"
        ));

        subjectChapters.put("Physics", Arrays.asList("Mechanics", "Thermodynamics", "Optics", "Electromagnetism"));
        subjectChapters.put("Chemistry", Arrays.asList("Organic Chemistry", "Inorganic Chemistry", "Physical Chemistry"));
        subjectChapters.put("Biology", Arrays.asList("Botany", "Zoology", "Genetics", "Human Physiology"));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // or finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
