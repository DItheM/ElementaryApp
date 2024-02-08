package com.example.elementaryapp.recycler_view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Lesson;
import com.example.elementaryapp.classes.ObjectItem;

import java.util.ArrayList;

//Adapting recycler view
//get data from main activity using a constructer
public class RecycleViewAdapterObjects extends RecyclerView.Adapter<ViewHolderObjects>{

    Context context;
    ArrayList<ObjectItem> list;

    //constructor
    public RecycleViewAdapterObjects(Context context, ArrayList<ObjectItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderObjects onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.object_item, parent, false);
        return new ViewHolderObjects(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderObjects holder, int position) {
        ObjectItem object = list.get(position);
        String mainText = object.count + " " + object.name;
        holder.text.setText(mainText);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

//viewholder for all the item elements and functions
class ViewHolderObjects extends RecyclerView.ViewHolder{

    TextView text;

    private RecycleViewAdapterObjects adapter;

    public ViewHolderObjects(@NonNull View itemView) {
        super(itemView);

        text = itemView.findViewById(R.id.item_name);
    }

    //linking the adapter
    public ViewHolderObjects linkAdapter (RecycleViewAdapterObjects adapter) {
        this.adapter = adapter;
        return this;
    }
}
