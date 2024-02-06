package com.example.elementaryapp.content;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

        Intent intent = getIntent();
        // Retrieve data from the Intent
        int type = intent.getIntExtra("type", 0);

        recyclerView = findViewById(R.id.recycler_view);
        chooseBtn = findViewById(R.id.choose_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        int bgClr;

        if (type == 0) {
            Lesson lesson_1 = new Lesson(R.drawable.draw_img, "Write numbers 1 to 9", "Delightful introduction to the world of numbers", "draw", 0);
            list.add(lesson_1);

            Lesson lesson_2 = new Lesson(R.drawable.i_2, "Watch tutorials", "Step-by-step video tutorials on how to write numbers 1 to 9", "tutorial", 0);
            list.add(lesson_2);

            Lesson lesson_3 = new Lesson(R.drawable.test_png, "Get a test", "Challenge young minds with a series of engaging tasks", "quiz", -1);
            list.add(lesson_3);

            bgClr = R.color.bgClr_1;
        } else if (type == 1) {
            Lesson lesson_1 = new Lesson(R.drawable.draw_img, "Write sinhala letters", "Delightful introduction to the world of sinhala letters", "draw", 1);
            list.add(lesson_1);

            Lesson lesson_2 = new Lesson(R.drawable.i_2, "Watch tutorials", "Step-by-step video tutorials on how to write sinhala letters", "tutorial", 1);
            list.add(lesson_2);

            bgClr = R.color.bgClr_3;
        } else {
            Lesson lesson_1 = new Lesson(R.drawable.animals, "Identify animals", "Delightful introduction to the world of animals", "detect", 0);
            list.add(lesson_1);

            Lesson lesson_2 = new Lesson(R.drawable.objects, "Identify objects", "Delightful introduction to the world of objects", "detect", 1);
            list.add(lesson_2);

            bgClr = R.color.bgClr_2;
        }


        adapter = new RecycleViewAdapterLessons(this, list, recyclerView, bgClr, chooseBtn);
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

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lesson lesson = adapter.getIntentData();
                if (lesson != null) {
                    if (lesson.pageType.equals("draw")) {
                        Intent intent = new Intent(LessonsScreenActivity.this, DrawScreenActivity.class);
                        intent.putExtra("type", lesson.subType);
                        startActivity(intent);
                    } else if (lesson.pageType.equals("detect")) {
                        Intent intent = new Intent(LessonsScreenActivity.this, IdentifyScreenActivity.class);
                        intent.putExtra("type", lesson.subType);
                        startActivity(intent);
                    } else if (lesson.pageType.equals("quiz")) {
                        Intent intent = new Intent(LessonsScreenActivity.this, MathQuizScreenActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}