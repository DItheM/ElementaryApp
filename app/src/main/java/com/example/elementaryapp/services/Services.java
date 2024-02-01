package com.example.elementaryapp.services;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.example.elementaryapp.R;

public class Services {
    //back button functionality
    public static void onPressBack (Activity activity) {
        ImageView btn = activity.findViewById(R.id.back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
}
