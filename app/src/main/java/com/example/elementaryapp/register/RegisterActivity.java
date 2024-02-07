package com.example.elementaryapp.register;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.example.elementaryapp.R;
import com.example.elementaryapp.content.MenuMainActivity;
import com.example.elementaryapp.database.DatabaseHelper;

import java.io.File;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText nameInput;
    ImageView img_1, img_2, img_3, img_4, img_5, pfp;
    Button uploadPfpBtn, createProfileBtn;
    AlertDialog alertDialog;
    int imageId = R.drawable.profile_default;
    private DrawerLayout drawerLayout;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> cropLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createProfileBtn = findViewById(R.id.create_profile_btn);
        uploadPfpBtn = findViewById(R.id.upload_pfp_btn);
        nameInput = findViewById(R.id.nameInput);
        pfp = findViewById(R.id.pfp);
        databaseHelper = new DatabaseHelper(this);

        // Set max length to 10 characters
        int maxLength = 10;
        nameInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        // Capitalize first letter
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    // Capitalize first letter
                    String text = s.toString();
                    String capitalizedText = Character.toUpperCase(text.charAt(0)) + text.substring(1);
                    if (!capitalizedText.equals(text)) {
                        nameInput.setText(capitalizedText);
                        nameInput.setSelection(nameInput.length()); // Move cursor to end
                    }
                }
            }
        });
        createProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProfile();
            }
        });

        // Initialize the image picker launcher
//        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        // Handle the selected image URI
//                        Uri selectedImageUri = result.getData().getData();
//                        // Start the image cropping activity
//                    }
//                });
        uploadPfpBtn.setOnClickListener(v -> ShowAlertDialog());
    }

    public void addClickListeners(ImageView imageView) {
        imageView.setOnClickListener(v -> {
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    pfp.setImageBitmap(bitmap);
                    getImageId(imageView);
                }
            }
            alertDialog.dismiss();
        });
    }

    public void ShowAlertDialog() {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentAlertDialog);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.gallery_dialog_layout, null);
        builder.setView(customLayout);

        // Set up your custom views and actions
        Button btn = customLayout.findViewById(R.id.close_btn);
        img_1 = customLayout.findViewById(R.id.img_1);
        img_2 = customLayout.findViewById(R.id.img_2);
        img_3 = customLayout.findViewById(R.id.img_3);
        img_4 = customLayout.findViewById(R.id.img_4);
        img_5 = customLayout.findViewById(R.id.img_5);

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
        addClickListeners(img_1);
        addClickListeners(img_2);
        addClickListeners(img_3);
        addClickListeners(img_4);
        addClickListeners(img_5);
    }

    // Method to open the image picker
//    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        imagePickerLauncher.launch(intent);
//    }

    public void getImageId(ImageView imageView) {
        // Get the ID of the clicked ImageView
        int resourceId = imageView.getId();
        // Get the resource name associated with the ID
        String resourceName = getResources().getResourceName(resourceId);
        if (Objects.equals(resourceName, "img_2")) {
            imageId = R.drawable.pfp_1;
        } else if (Objects.equals(resourceName, "img_3")) {
            imageId = R.drawable.pfp_2;
        } else if (Objects.equals(resourceName, "img_4")) {
            imageId = R.drawable.pfp_3;
        } else if (Objects.equals(resourceName, "img_5")) {
            imageId = R.drawable.pfp_4;
        }
    }

    public void createProfile() {
        String inputText = nameInput.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            databaseHelper.insertData("1", inputText, imageId);
            Intent intent = new Intent(RegisterActivity.this, MenuMainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}