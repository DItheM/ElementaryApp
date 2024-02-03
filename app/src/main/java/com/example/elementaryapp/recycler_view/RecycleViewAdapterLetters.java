package com.example.elementaryapp.recycler_view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Lesson;
import com.example.elementaryapp.classes.Letter;

import java.util.ArrayList;

//Adapting recycler view
//get data from main activity using a constructer
public class RecycleViewAdapterLetters extends RecyclerView.Adapter<ViewHolderLetters>{

    Context context;
    ArrayList<Letter> list;

    int selectedPosition = 0;
    int bgClr;
    RecyclerView recyclerView;


    //constructor
    public RecycleViewAdapterLetters(Context context, ArrayList<Letter> list, RecyclerView recyclerView, int bgClr) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
        this.bgClr = bgClr;
    }

    @NonNull
    @Override
    public ViewHolderLetters onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.letter_item, parent, false);
        return new ViewHolderLetters(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLetters holder, int position) {
        Letter letter = list.get(position);
        holder.number.setText(letter.letter);

        // Set background color based on the selected position
        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), bgClr)); // Change the color as needed
            holder.number.setTextColor(Color.WHITE);
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE); // Reset to default
            holder.number.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.Heading));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

//viewholder for all the item elements and functions
class ViewHolderLetters extends RecyclerView.ViewHolder{

    TextView number;
    CardView cardView;

    private RecycleViewAdapterLetters adapter;

    public ViewHolderLetters(@NonNull View itemView) {
        super(itemView);

        number = itemView.findViewById(R.id.number);
        cardView = itemView.findViewById(R.id.card_view);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.selectedPosition = getAdapterPosition();
                adapter.notifyDataSetChanged();
            }
        });

    }

    //linking the adapter
    public ViewHolderLetters linkAdapter (RecycleViewAdapterLetters adapter) {
        this.adapter = adapter;
        return this;
    }
}
