package com.example.elementaryapp.content;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;


import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Letter;
import com.example.elementaryapp.recycler_view.RecycleViewAdapterLetters;
import com.example.elementaryapp.services.DrawingView;
import com.example.elementaryapp.services.Services;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import okhttp3.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DrawScreenActivity extends AppCompatActivity {

    private DrawingView drawingView;
    RecycleViewAdapterLetters adapter;

    RecyclerView recyclerView;

    ArrayList<Letter> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_screen);

        drawingView = findViewById(R.id.drawing_view);
        FloatingActionButton clearButton = findViewById(R.id.btn_clear);
        Button checkButton = findViewById(R.id.btn_check);

        Services.onPressBack(this);

        Intent intent = getIntent();
        // Retrieve data from the Intent
        int type = intent.getIntExtra("type", 0);

        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        int bgClr;

        if (type == 0) {
            for (int i = 0; i <= 9; i++) {
                list.add(new Letter(String.valueOf(i)));
            }
            bgClr = R.color.bgClr_1;
        } else {
            for (int i = 11; i <= 20; i++) {
                list.add(new Letter(String.valueOf(i)));
            }
            bgClr = R.color.bgClr_3;
        }


        adapter = new RecycleViewAdapterLetters(this, list, recyclerView, bgClr);
        recyclerView.setAdapter(adapter);

        clearButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnBack)));
        clearButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        checkButton.setBackgroundColor(getResources().getColor(bgClr));


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the clearCanvas() method of your DrawingView
                drawingView.clearCanvas();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the drawn image from the DrawingView
                Bitmap bitmap = drawingView.getBitmap();

                // Convert Bitmap to byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                // Create an OkHttpClient instance
                OkHttpClient client = new OkHttpClient();

                // Build the request body
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", "image.png", RequestBody.create(byteArray, MediaType.get("image/png")));

                RequestBody requestBody = builder.build();

                // Build the HTTP request
                Request request = new Request.Builder()
                        .url("http://192.168.8.102:5000/predict")
                        .post(requestBody)
                        .build();

                // Make the API call asynchronously
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("API_REQUEST", "Failed: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            final String responseData = response.body().string();

                            // Update UI on the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Process the response data (prediction result) and update the UI
                                    // Example: display the prediction result in a TextView
                                    TextView resultTextView = findViewById(R.id.result_text_view);
                                    resultTextView.setText(responseData);
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
