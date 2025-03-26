package com.example.intelclassone;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get subject name from Intent
        String subject = getIntent().getStringExtra("subject_name");
        subjectTextView.setText(subject);

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

        subjectChapters.put("Maths", Arrays.asList("Algebra", "Trigonometry", "Calculus", "Probability"));
        subjectChapters.put("Physics", Arrays.asList("Mechanics", "Thermodynamics", "Optics", "Electromagnetism"));
        subjectChapters.put("Chemistry", Arrays.asList("Organic Chemistry", "Inorganic Chemistry", "Physical Chemistry"));
        subjectChapters.put("Biology", Arrays.asList("Botany", "Zoology", "Genetics", "Human Physiology"));

    }
}
