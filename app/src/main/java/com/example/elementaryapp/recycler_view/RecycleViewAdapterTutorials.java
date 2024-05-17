package com.example.elementaryapp.recycler_view;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Tutorial;
import com.example.elementaryapp.content.LessonsScreenActivity;
import com.example.elementaryapp.content.MathQuizScreenActivity;
import com.example.elementaryapp.content.WatchVideoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//Adapting recycler view
//get data from main activity using a constructer
public class RecycleViewAdapterTutorials extends RecyclerView.Adapter<ViewHolderTutorials>{

    Context context;
    ArrayList<Tutorial> list;

    int bg_clr;
    RecyclerView recyclerView;
    FrameLayout videoContainer;




    //constructor
    public RecycleViewAdapterTutorials(Context context, ArrayList<Tutorial> list, RecyclerView recyclerView, FrameLayout videoContainer, int bg_clr) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
        this.videoContainer = videoContainer;
        this.bg_clr = bg_clr;
    }

    @NonNull
    @Override
    public ViewHolderTutorials onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tutorial_item, parent, false);
        return new ViewHolderTutorials(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTutorials holder, int position) {
        Tutorial tutorial = list.get(position);
        holder.header.setText(tutorial.name);
        holder.imageView.setImageResource(tutorial.thumbnail);
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), bg_clr));

    }

    // Helper method to check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

//viewholder for all the item elements and functions
class ViewHolderTutorials extends RecyclerView.ViewHolder {

    TextView header;
    ImageView imageView;
    CardView cardView, videoCardView;
    Button watch_btn;

    private RecycleViewAdapterTutorials adapter;

    public ViewHolderTutorials(@NonNull View itemView) {
        super(itemView);

//        number = itemView.findViewById(R.id.number);
        header = itemView.findViewById(R.id.header);
        cardView = itemView.findViewById(R.id.card_bg);
        videoCardView = itemView.findViewById(R.id.videoCardView);
        watch_btn = itemView.findViewById(R.id.watch_btn);
        imageView = itemView.findViewById(R.id.imageView);
        watch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                // Dismiss the dialog or perform any other action
                Intent intent = new Intent(adapter.context, WatchVideoActivity.class);
                Tutorial tutorial = adapter.list.get(getAdapterPosition());
                intent.putExtra("link", tutorial.link);
                adapter.context.startActivity(intent);
            }
        });
    }


    //linking the adapter
    public ViewHolderTutorials linkAdapter(RecycleViewAdapterTutorials adapter) {
        this.adapter = adapter;
        return this;
    }
}