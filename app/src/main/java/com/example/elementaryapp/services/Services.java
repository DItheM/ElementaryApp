package com.example.elementaryapp.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.elementaryapp.R;
import com.google.android.gms.cast.framework.media.ImagePicker;

public class Services {

    public static String ipAddress = "http://192.168.5.62:5000";

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

    //open image picker
    public static void openImagePicker (Fragment fragment) {
        
    }

    //upload image to firebase
    public static void uploadImage (Uri uri, ImageView pfp, Context context, int type) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait while Updating Image...");
        progressDialog.setTitle("Update Image");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


    }

}
