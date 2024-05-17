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
            Lesson lesson_1 = new Lesson(R.drawable.i_2, "wxl ,shk úÈh", "wxl ,shkafka fldfyduo lsh, ùäfhda weiqfrka n,uq'", "tutorial", 0);
            list.add(lesson_1);

            Lesson lesson_2 = new Lesson(R.drawable.draw_img, "wxl ,sùu", ".‚; wxl .ek ir,j bf.k.uq'", "draw", 0);
            list.add(lesson_2);

            Lesson lesson_3 = new Lesson(R.drawable.test_png, "ir, m%YaK", "ir, .‚; .eg¨ úi|uq'", "quiz", -1);
            list.add(lesson_3);

            bgClr = R.color.bgClr_1;
        } else if (type == 1) {
            Lesson lesson_1 = new Lesson(R.drawable.draw_img, "isxy, wl=re ,shuq", "isxy, wl=re .ek ir,j bf.k.uq'", "draw", 1);
            list.add(lesson_1);

//            Lesson lesson_2 = new Lesson(R.drawable.i_2, "wl=re ,shk úÈh", "wl=re ,shkafka fldfyduo lsh, ùäfhda weiqfrka n,uq'", "tutorial", 1);
//            list.add(lesson_2);

            bgClr = R.color.bgClr_3;
        } else {
            Lesson lesson_1 = new Lesson(R.drawable.animals, "i;=ka y÷kd.ekSu", "Tn olsk i;=ka f,aisfhka w÷k.kak'", "detect", 0);
            list.add(lesson_1);

            Lesson lesson_2 = new Lesson(R.drawable.objects, "jia;+ka y÷kd.ekSu", "Tn olsk jia;+ka f,aisfhka w÷k.kak'", "detect", 1);
            list.add(lesson_2);

            Lesson lesson_3 = new Lesson(R.drawable.vr, "Tfí f,dalfhka Tíng", "T.afukagâ ßhe,sá ;dlaIKfhka i;=ka Èyd n,kak'", "vr", -1);
            list.add(lesson_3);

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
                    } else if (lesson.pageType.equals("tutorial")) {
                        Intent intent = new Intent(LessonsScreenActivity.this, TutorialScreenActivity.class);
                        intent.putExtra("type", lesson.subType);
                        startActivity(intent);
                    } else if (lesson.pageType.equals("detect")) {
                        Intent intent = new Intent(LessonsScreenActivity.this, IdentifyScreenActivity.class);
                        intent.putExtra("type", lesson.subType);
                        startActivity(intent);
                    } else if (lesson.pageType.equals("quiz")) {
                        Intent intent = new Intent(LessonsScreenActivity.this, MathQuizScreenActivity.class);
                        startActivity(intent);
                    } else if (lesson.pageType.equals("vr")) {
                        Intent intent = new Intent(LessonsScreenActivity.this, VrSceenActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}