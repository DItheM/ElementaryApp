package com.example.elementaryapp.content;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Tutorial;
import com.example.elementaryapp.recycler_view.RecycleViewAdapterTutorials;
import com.example.elementaryapp.services.Services;

import java.util.ArrayList;

public class TutorialScreenActivity extends AppCompatActivity {

    RecycleViewAdapterTutorials adapter;

    RecyclerView recyclerView;

    ArrayList<Tutorial> list;

    FrameLayout videoContainer;

    String[] links = {
            "https://www.youtube.com/embed/TYc5JZkvrqM?si=ULhgo3V_FhV1tRg0",
            "https://www.youtube.com/embed/zTa9Htgzxgo?si=qkzrGB9slF2i4eyo",
            "https://www.youtube.com/embed/YJwFXRcevNY?si=X1OTVrmnkHb3b-on",
            "https://www.youtube.com/embed/Cx9F6oXHjSU?si=0_0f2lbeIO6XW_IT",
            "https://www.youtube.com/embed/2rgmoxVM5Gs?si=vDJ5oYFoPZQN7d5g",
            "https://www.youtube.com/embed/UaoHsQjkfN4?si=gnRdX9vdUbNrF-yZ",
            "https://www.youtube.com/embed/egXYBEvitlU?si=u-oS33vDjpyD6Mg-",
            "https://www.youtube.com/embed/mlyTCSaeanU?si=6lYBHGdDzhdzNrfZ",
            "https://www.youtube.com/embed/Cl-XFEiZTV8?si=UcdrRSUahsJ58B1d",
            "https://www.youtube.com/embed/Ovf0sGrA6TU?si=I1e53InTKWgysRG8"
    };

    int[] thumbnails = {
            R.drawable.thumbnail_0,
            R.drawable.thumbnail_1,
            R.drawable.thumbnail_2,
            R.drawable.thumbnail_3,
            R.drawable.thumbnail_4,
            R.drawable.thumbnail_5,
            R.drawable.thumbnail_6,
            R.drawable.thumbnail_7,
            R.drawable.thumbnail_8,
            R.drawable.thumbnail_9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);

        Services.onPressBack(this);

        Intent intent = getIntent();
        // Retrieve data from the Intent
        int type = intent.getIntExtra("type", 0);

        recyclerView = findViewById(R.id.recycler_view);
        videoContainer = findViewById(R.id.videoContainer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        int bgClr = R.color.bgClr_1;

        if (type == 0) {
            for (int i = 0;links.length > i; i++) {
                list.add(new Tutorial("wxl " + i + " ,shk úÈh n,uq", links[i], thumbnails[i]));
            }
        }
//        else {
//
//            list.add(new Tutorial("How to write letter a", "https://www.youtube.com/embed/V2KCAfHjySQ?si=3nYbPysuA7GnAwdW"));
//            list.add(new Tutorial("How to write letter aaa", ""));
//
//            bgClr = R.color.bgClr_3;
//
//        }

        adapter = new RecycleViewAdapterTutorials(this, list, recyclerView, videoContainer, bgClr);
        recyclerView.setAdapter(adapter);


    }
}