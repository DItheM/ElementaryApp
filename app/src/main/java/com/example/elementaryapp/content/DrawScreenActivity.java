package com.example.elementaryapp.content;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;


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
import java.util.Objects;

public class DrawScreenActivity extends AppCompatActivity {

    private DrawingView drawingView;
    RecycleViewAdapterLetters adapter;

    RecyclerView recyclerView;

    ArrayList<Letter> list;
    String selectedLetter;

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

        selectedLetter = list.get(0).letter;

        adapter = new RecycleViewAdapterLetters(this, list, recyclerView, bgClr);
        recyclerView.setAdapter(adapter);

        clearButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnBack)));
        clearButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        checkButton.setBackgroundColor(getResources().getColor(bgClr));

        adapter.setOnItemClickListener(new RecycleViewAdapterLetters.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedLetter = list.get(position).letter;
                Toast.makeText(DrawScreenActivity.this, "You selected: "+selectedLetter, Toast.LENGTH_SHORT).show();
            }
        });


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

                // Check if anything is drawn
                if (isBitmapNotEmpty(bitmap)) {
                    Toast.makeText(DrawScreenActivity.this, "Validating", Toast.LENGTH_SHORT).show();

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
                            .url("http://192.168.5.62:5000/predict")
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
                                        if (Objects.equals(selectedLetter, responseData)) {
                                            ShowAlertDialog( bgClr, 0);
                                        } else {
                                            ShowAlertDialog( bgClr, 1);
                                        }

                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(DrawScreenActivity.this, "Drawer board is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ShowAlertDialog(int bgClr, int type) {
        AlertDialog alertDialog;

        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentAlertDialog);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
        builder.setView(customLayout);

        // Set up your custom views and actions
        TextView Header = customLayout.findViewById(R.id.heading);
        TextView subText = customLayout.findViewById(R.id.sub_text);
        ImageView imageView = customLayout.findViewById(R.id.dialog_image);
        Button btn = customLayout.findViewById(R.id.button);

        if (type == 0) {
            Header.setText("Correct!");
            subText.setText("You done it champ, congratulations!");
            imageView.setImageResource(R.drawable.thumbs_up);
            btn.setText("Thanks!");
        } else {
            Header.setText("Not Correct!");
            subText.setText("Try again champ, you can do it!");
            imageView.setImageResource(R.drawable.try_again);
            btn.setText("Try again");
        }

        btn.setBackgroundColor(bgClr);

        // Create and show the dialog
        alertDialog = builder.create();
        alertDialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                // Dismiss the dialog or perform any other action
                alertDialog.dismiss();
            }
        });
    }

    // Method to check if the Bitmap is not empty (something is drawn)
    private boolean isBitmapNotEmpty(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Iterate through each pixel of the Bitmap
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = bitmap.getPixel(x, y);

                // Compare the pixel value with a default or background color
                // For example, check if it's not equal to Color.WHITE
                if (pixel != Color.WHITE) {
                    return true; // At least one non-default pixel found, something is drawn
                }
            }
        }

        // No non-default pixels found, the Bitmap is considered empty
        return false;
    }
}
