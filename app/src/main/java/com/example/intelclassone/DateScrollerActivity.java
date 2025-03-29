package com.example.intelclassone;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateScrollerActivity extends AppCompatActivity {

    private TextView monthTextView;
    private RecyclerView dateRecyclerView;
    private DateAdapter dateAdapter;
    private List<DateModel> dateList = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_scroller);

        monthTextView = findViewById(R.id.monthTextView);
        dateRecyclerView = findViewById(R.id.dateRecyclerView);

        // Set layout manager
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dateRecyclerView.setLayoutManager(layoutManager);

        // Generate date list starting from today
        generateDates();

        // Set adapter
        dateAdapter = new DateAdapter(dateList);
        dateRecyclerView.setAdapter(dateAdapter);

        // Scroll to today's date
        scrollToToday();

        // Update month dynamically when scrolling
        dateRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem >= 0) {
                    monthTextView.setText(dateList.get(firstVisibleItem).getMonthYear());
                }
            }
        });
    }

    private void generateDates() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.getDefault());
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        // Generate dates for current and next month
        for (int i = 0; i < 60; i++) {
            String day = dayFormat.format(calendar.getTime());
            String monthYear = monthYearFormat.format(calendar.getTime());
            dateList.add(new DateModel(day, monthYear));
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        }
    }

    private void scrollToToday() {
        int todayPosition = 0;
        for (int i = 0; i < dateList.size(); i++) {
            if (dateList.get(i).getDay().equals(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))) {
                todayPosition = i;
                break;
            }
        }
        dateRecyclerView.scrollToPosition(todayPosition);
        monthTextView.setText(dateList.get(todayPosition).getMonthYear());
    }
}

