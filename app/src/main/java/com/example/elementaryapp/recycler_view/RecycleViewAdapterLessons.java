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

import java.util.ArrayList;

//Adapting recycler view
//get data from main activity using a constructer
public class RecycleViewAdapterLessons extends RecyclerView.Adapter<ViewHolderLessons>{

    Context context;
    ArrayList<Lesson> list;

    int selectedPosition = 0;
    RecyclerView recyclerView;


    //constructor
    public RecycleViewAdapterLessons(Context context, ArrayList<Lesson> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolderLessons onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);
        return new ViewHolderLessons(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLessons holder, int position) {
        Lesson lesson = list.get(position);
        holder.header.setText(lesson.header);
        holder.subText.setText(lesson.subText);
        holder.image.setImageResource(lesson.image);

        // Set background color based on the selected position
        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bgClr_1)); // Change the color as needed
            holder.header.setTextColor(Color.WHITE);
            holder.subText.setTextColor(Color.WHITE);
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE); // Reset to default
            holder.header.setTextColor(Color.BLACK);
            holder.subText.setTextColor(Color.BLACK);
        }

//        if (position == getItemCount() - 1) {
//
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int highlightCenterItem() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            int firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
            int centerPosition = (firstVisibleItemPosition + lastVisibleItemPosition) / 2;

            // Update the selected position based on the center item
            selectedPosition = centerPosition;
            notifyDataSetChanged(); // Refresh the entire RecyclerView to apply the color changes
        }
        return selectedPosition;
    }
}

//viewholder for all the item elements and functions
class ViewHolderLessons extends RecyclerView.ViewHolder{

    TextView header, subText;

    ImageView image;

    CardView cardView;

    private RecycleViewAdapterLessons adapter;

    public ViewHolderLessons(@NonNull View itemView) {
        super(itemView);

        header = itemView.findViewById(R.id.header);
        subText = itemView.findViewById(R.id.sub_text);
        image = itemView.findViewById(R.id.image);
        cardView = itemView.findViewById(R.id.card_view);


        //triggers when delete button clicked
        //after successful completion from database, item will remove from recycle view
//        itemView.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos = getAdapterPosition();
//                if (pos != RecyclerView.NO_POSITION) {
//
//                }
//            }
//        });
    }

    //linking the adapter
    public ViewHolderLessons linkAdapter (RecycleViewAdapterLessons adapter) {
        this.adapter = adapter;
        return this;
    }
}
