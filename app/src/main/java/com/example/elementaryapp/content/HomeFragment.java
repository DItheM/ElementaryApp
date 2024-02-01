package com.example.elementaryapp.content;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.elementaryapp.R;
import com.example.elementaryapp.services.Services;

public class HomeFragment extends Fragment {

    Button mathBtn, sinhalaBtn, envBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mathBtn = v.findViewById(R.id.math_btn);
        sinhalaBtn = v.findViewById(R.id.sinhala_btn);
        envBtn = v.findViewById(R.id.env_btn);

        //set on click listener for maths button button
        mathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LessonsScreenActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}