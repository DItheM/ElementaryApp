package com.example.elementaryapp.content;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.elementaryapp.R;
import com.example.elementaryapp.services.Services;

public class LessonsScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_screen);

        Services.onPressBack(this);
    }
}