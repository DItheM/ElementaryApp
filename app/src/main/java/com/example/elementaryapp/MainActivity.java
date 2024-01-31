package com.example.elementaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;


import okhttp3.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.elementaryapp.DrawingView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = findViewById(R.id.drawing_view);
        Button clearButton = findViewById(R.id.btn_clear);
        Button checkButton = findViewById(R.id.btn_check);

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
