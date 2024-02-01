package com.example.elementaryapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.elementaryapp.R;
import com.example.elementaryapp.content.HomeFragment;
import com.example.elementaryapp.content.MenuMainActivity;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button startLearningBtn = findViewById(R.id.start_learning_btn);

        startLearningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MenuMainActivity.class);
                startActivity(intent);
            }
        });
    }
}