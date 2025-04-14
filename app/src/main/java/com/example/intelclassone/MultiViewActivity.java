package com.example.intelclassone;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;


public class MultiViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MultiViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multi_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<MultiViewItem> items = Arrays.asList(
                new MultiViewItem(MultiViewItem.TYPE_HEADER, "Welcome!"),
                new MultiViewItem(MultiViewItem.TYPE_CONTENT, "Math - Chapter 1"),
                new MultiViewItem(MultiViewItem.TYPE_CONTENT, "Math - Chapter 2"),
                new MultiViewItem(MultiViewItem.TYPE_AD, "Buy Premium"),
                new MultiViewItem(MultiViewItem.TYPE_CONTENT, "Science - Chapter 1"),
                new MultiViewItem(MultiViewItem.TYPE_HEADER, "End of List")
        );

        adapter = new MultiViewAdapter(items);
        recyclerView.setAdapter(adapter);
    }
}
