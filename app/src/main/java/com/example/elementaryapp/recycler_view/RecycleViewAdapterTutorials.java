package com.example.elementaryapp.recycler_view;

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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Tutorial;

import java.util.ArrayList;
import java.util.HashSet;

//Adapting recycler view
//get data from main activity using a constructer
public class RecycleViewAdapterTutorials extends RecyclerView.Adapter<ViewHolderTutorials>{

    Context context;
    ArrayList<Tutorial> list;

    int bg_clr;
    RecyclerView recyclerView;
    FrameLayout videoContainer;

    HashSet<String> loadedUrls = new HashSet<>();




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
//        Uri videoUri = Uri.parse("android.resource://" + "com.example.elementaryapp" + "/" + tutorial.link);
//        holder.webView.setVideoURI(videoUri);
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), bg_clr));
        if (isNetworkAvailable()) {
            holder.webView.setVisibility(View.VISIBLE);
            holder.errorTextView.setVisibility(View.GONE);

            // Check if the URL has been loaded before
            if (!loadedUrls.contains(tutorial.link)) {
                // Load the URL
                holder.webView.loadUrl(tutorial.link);
                // Add the URL to the loaded URLs set
                loadedUrls.add(tutorial.link);
            }
        } else {
            holder.webView.setVisibility(View.GONE);
            holder.errorTextView.setVisibility(View.VISIBLE);
        }


//        holder.webView.loadData(tutorial.link, "text/html", "utf-8");
//        holder.webView.getSettings().setJavaScriptEnabled(true);
//        holder.webView.setWebChromeClient(new WebChromeClient());
//        holder.webView.loadUrl("https://www.youtube.com");

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
    WebView webView;
    CardView cardView, videoCardView;
    WebChromeClient.CustomViewCallback customViewCallback;
    View customView;
    ProgressBar progressBar;
    TextView errorTextView;

    private RecycleViewAdapterTutorials adapter;

    public ViewHolderTutorials(@NonNull View itemView) {
        super(itemView);

//        number = itemView.findViewById(R.id.number);
        header = itemView.findViewById(R.id.header);
        cardView = itemView.findViewById(R.id.card_bg);
        videoCardView = itemView.findViewById(R.id.videoCardView);
        progressBar = itemView.findViewById(R.id.progressBar);
        errorTextView = itemView.findViewById(R.id.errorTextView);
//        youTubePlayerView = itemView.findViewById(R.id.youtube_player);
//        // Access the lifecycle owner from the RecyclerView
//        lifecycleOwner = (LifecycleOwner) itemView.getContext();
//        // Set YouTubePlayerView's lifecycle owner
//        lifecycleOwner.getLifecycle().addObserver(youTubePlayerView);
        webView = itemView.findViewById(R.id.webView);


        // Media controller to enable play, pause, forward, etc. options.
//        MediaController mediaController = new MediaController(adapter.context);
//        videoView.setMediaController(mediaController);
//        mediaController.setAnchorView(videoView);
//
//        videoView.start();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);



        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (customView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                customView = view;
                customViewCallback = callback;
                adapter.videoContainer.addView(view);
                adapter.videoContainer.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onHideCustomView() {
                if (customView == null) return;
                adapter.videoContainer.removeView(customView);
                adapter.videoContainer.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                customView = null;
                customViewCallback.onCustomViewHidden();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("youtube.com") || url.contains("youtu.be")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Context context = view.getContext();
                    context.startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webView.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                // Attempt to reload the WebView after a short delay
                webView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Show progress bar while reloading
                        progressBar.setVisibility(View.VISIBLE);
                        // Reload the WebView
                        webView.reload();
                    }
                }, 1000);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

    //linking the adapter
    public ViewHolderTutorials linkAdapter(RecycleViewAdapterTutorials adapter) {
        this.adapter = adapter;
        return this;
    }
}