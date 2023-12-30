package com.example.elementaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.elementaryapp.DrawingView;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = findViewById(R.id.drawing_view);
        Button clearButton = findViewById(R.id.btn_clear);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the clearCanvas() method of your DrawingView
                drawingView.clearCanvas();
            }
        });
    }

    // Function to clear the canvas when the "Clear" button is clicked
//    public void clearCanvas(View view) {
//        drawingView.clearCanvas();
//    }
}
