package com.example.elementaryapp.content;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Lesson;
import com.example.elementaryapp.classes.ObjectItem;
import com.example.elementaryapp.recycler_view.RecycleViewAdapterLessons;
import com.example.elementaryapp.recycler_view.RecycleViewAdapterObjects;
import com.example.elementaryapp.services.Services;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.*;

public class IdentifyScreenActivity extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ImageCapture imageCapture;

    int type;
    String url;
    ArrayList<ObjectItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_screen);

        Services.onPressBack(this);

        Intent intent = getIntent();
        // Retrieve data from the Intent
        type = intent.getIntExtra("type", 0);

        if (type == 0) {
            url = Services.ipAddress + "/detect_animal";
        } else {
            url = Services.ipAddress + "/detect_object";
        }

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        // Initialize the imageCapture use case
        imageCapture = new ImageCapture.Builder().build();

        // Bind the camera when permission is granted
        requestCameraPermission();

        // Button to capture an image
        Button captureButton = findViewById(R.id.capture_btn);
        captureButton.setOnClickListener(v -> captureImage());
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        PreviewView previewView = findViewById(R.id.cameraPreviewView);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Unbind the use cases before rebinding
        cameraProvider.unbindAll();

        // Bind the camera and imageCapture use case
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
    }

    private void captureImage() {
        // Set up output file to save the captured image
        File outputDirectory = getOutputDirectory();
        File photoFile = new File(outputDirectory, System.currentTimeMillis() + ".jpg");

        // Create the output options for the ImageCapture use case
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        // Capture the image
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        // Image capture success
                        Toast.makeText(IdentifyScreenActivity.this, "Image captured", Toast.LENGTH_SHORT).show();

                        // Send the captured image to the Flask server
                        sendImageToServer(photoFile);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        // Image capture failure
                        exception.printStackTrace();
                    }
                });
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Camera permission is already granted
            bindCamera();
        }
    }

    private void bindCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // Handle the exception appropriately
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private File getOutputDirectory() {
        // Get the directory for storing images. You can customize this based on your requirements.
        // In this example, it uses the app's cache directory.
        File mediaDir = getExternalMediaDirs()[0];
        File appDir = new File(mediaDir, getString(R.string.app_name));
        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                return getFilesDir();
            }
        }
        return appDir;
    }

    private void sendImageToServer(File imageFile) {
        Toast.makeText(this, "Identifying", Toast.LENGTH_SHORT).show();
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageFile.getName(), RequestBody.create(imageFile, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Handle the server's response
                if (response.isSuccessful()) {
                    list = new ArrayList<>();
                    imageFile.delete();
                    // Server successfully received the image
                    // You can process the server's response here
                    String responseBody = response.body().string();
                    // Parse the JSON response
                    try {
                        String prediction;
                        String confidence;
                        if (type == 0) {
                            JSONObject json = new JSONObject(responseBody);
                            prediction = json.getString("prediction");
                            confidence = json.getString("confidence");

                        } else {
                            prediction = "";
                            confidence = "";
                            JSONArray jsonArray = new JSONArray(responseBody);
                            // Process the received array data
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONArray innerArray = jsonArray.getJSONArray(i);
                                    int count = innerArray.getInt(1);
                                    String name = innerArray.getString(0);
                                    list.add(new ObjectItem(name, count));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        runOnUiThread(() -> {
                            ShowAlertDialog(type, prediction, confidence);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void ShowAlertDialog(int type, String predictionVal, String confidenceVal) {
        if (type == 0) {
            animalDialog(predictionVal, confidenceVal);
        } else {
            objectDialog();
        }

    }

    public void animalDialog(String predictionVal, String confidenceVal) {
        AlertDialog alertDialog;

        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentAlertDialog);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.identify_dialog_layout, null);
        builder.setView(customLayout);

        // Set up your custom views and actions
        TextView prediction = customLayout.findViewById(R.id.prediction);
        TextView confidence = customLayout.findViewById(R.id.confidence);
        ImageView imageView = customLayout.findViewById(R.id.dialog_image);
        Button btn = customLayout.findViewById(R.id.button);

        String capitalizedPredictionVal = predictionVal.substring(0, 1).toUpperCase() + predictionVal.substring(1);
        String processedConfidenceVal = "Confidence level is " + confidenceVal;

        prediction.setText(capitalizedPredictionVal);
        confidence.setText(processedConfidenceVal);


        imageView.setImageResource(R.drawable.explorer);


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

    public void objectDialog() {
        AlertDialog alertDialog;
        RecycleViewAdapterObjects adapter;

        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentAlertDialog);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.object_dialog_layout, null);
        builder.setView(customLayout);

        // Set up your custom views and actions
        TextView error = customLayout.findViewById(R.id.no_found_text);
        Button btn = customLayout.findViewById(R.id.button);
        RecyclerView recyclerView = customLayout.findViewById(R.id.recyclerView);

        if (list.isEmpty()){
            error.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new RecycleViewAdapterObjects(this, list);
            recyclerView.setAdapter(adapter);

            error.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }


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

}
