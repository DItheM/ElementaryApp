package com.example.elementaryapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.elementaryapp.R;
import com.example.elementaryapp.content.MenuMainActivity;
import com.example.elementaryapp.database.DatabaseHelper;

public class SignInActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button startLearningBtn = findViewById(R.id.save_btn);
        databaseHelper = new DatabaseHelper(this);

        if(databaseHelper.isLoggedIn()) {
            Intent intent = new Intent(SignInActivity.this, MenuMainActivity.class);
            startActivity(intent);
            finish();
        } else {
            startLearningBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }
}