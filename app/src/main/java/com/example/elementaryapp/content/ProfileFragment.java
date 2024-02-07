package com.example.elementaryapp.content;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.elementaryapp.R;
import com.example.elementaryapp.database.DatabaseHelper;
import com.example.elementaryapp.register.RegisterActivity;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    DatabaseHelper databaseHelper;
    EditText nameInput;
    ImageView img_1, img_2, img_3, img_4, img_5, pfp;
    Button uploadPfpBtn, saveBtn;
    AlertDialog alertDialog;

    int imageId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        saveBtn = v.findViewById(R.id.save_btn);
        uploadPfpBtn = v.findViewById(R.id.upload_pfp_btn);
        nameInput = v.findViewById(R.id.nameInput);
        pfp = v.findViewById(R.id.pfp);
        databaseHelper = new DatabaseHelper(this.getContext());
        Cursor cursor = databaseHelper.getAllData();
        while (cursor.moveToNext()) {
            imageId = cursor.getInt(2);
            pfp.setImageResource(cursor.getInt(2));
            nameInput.setText(cursor.getString(1));
        }

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
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });
        uploadPfpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertDialog();
            }
        });

        return v;
    }

    public void addClickListeners(ImageView imageView, String resourceId) {
        imageView.setOnClickListener(v -> {
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    pfp.setImageBitmap(bitmap);
                }
            }
            getImageId(resourceId);
            alertDialog.dismiss();
        });
    }

    public void ShowAlertDialog() {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.TransparentAlertDialog);

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
        addClickListeners(img_1, "img_1");
        addClickListeners(img_2, "img_2");
        addClickListeners(img_3, "img_3");
        addClickListeners(img_4, "img_4");
        addClickListeners(img_5, "img_5");
    }

    public void getImageId(String resourceName) {
        if (Objects.equals(resourceName, "img_2")) {
            imageId = R.drawable.pfp_1;
        } else if (Objects.equals(resourceName, "img_3")) {
            imageId = R.drawable.pfp_2;
        } else if (Objects.equals(resourceName, "img_4")) {
            imageId = R.drawable.pfp_3;
        } else if (Objects.equals(resourceName, "img_5")) {
            imageId = R.drawable.pfp_4;
        } else {
            imageId = R.drawable.profile_default;
        }
    }

    public void saveProfile() {
        String inputText = nameInput.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this.getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            databaseHelper.updatePFPUrl("1", imageId);
            databaseHelper.updateUserName("1", inputText);
            nameInput.setText(inputText);
            Toast.makeText(this.getContext(), "Profile saved", Toast.LENGTH_SHORT).show();
        }
    }
}