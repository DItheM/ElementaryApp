package com.example.elementaryapp.content;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Lesson;
import com.example.elementaryapp.recycler_view.RecycleViewAdapterLessons;
import com.example.elementaryapp.services.Services;

import java.util.ArrayList;

public class LessonsScreenActivity extends AppCompatActivity {

    RecycleViewAdapterLessons adapter;

    RecyclerView recyclerView;

    ArrayList<Lesson> list;

    Button chooseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_screen);

        Services.onPressBack(this);

        recyclerView = findViewById(R.id.recycler_view);
        chooseBtn = findViewById(R.id.choose_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        Lesson lesson_1 = new Lesson(R.drawable.draw_img, "Write numbers 1 to 9", "Delightful introduction to the world of numbers");
        list.add(lesson_1);

        Lesson lesson_2 = new Lesson(R.drawable.i_2, "Watch tutorials", "Step-by-step video tutorials on how to write numbers 1 to 9");
        list.add(lesson_2);

        Lesson lesson_3 = new Lesson(R.drawable.test_png, "Get a test", "Challenge young minds with a series of engaging tasks");
        list.add(lesson_3);

        adapter = new RecycleViewAdapterLessons(this, list, recyclerView);
        recyclerView.setAdapter(adapter);

        // Add an OnScrollListener to highlight the center item
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = adapter.highlightCenterItem();
                if (position == -1) {
                    chooseBtn.setEnabled(false);
                    chooseBtn.setTextColor(Color.WHITE);
                    chooseBtn.setBackgroundColor(Color.GRAY);
                } else {
                    chooseBtn.setEnabled(true);
                    chooseBtn.setTextColor(Color.WHITE);
                    chooseBtn.setBackgroundColor(getResources().getColor(R.color.btnBack));
                }
            }
        });
    }
}