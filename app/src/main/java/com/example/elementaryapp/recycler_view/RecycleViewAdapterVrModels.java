package com.example.elementaryapp.recycler_view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Letter;
import com.example.elementaryapp.classes.VrModel;

import java.util.ArrayList;

//Adapting recycler view
//get data from main activity using a constructer
public class RecycleViewAdapterVrModels extends RecyclerView.Adapter<ViewHolderVrModels>{

    Context context;
    ArrayList<VrModel> list;

    int selectedPosition = 0;
    RecyclerView recyclerView;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }


    //constructor
    public RecycleViewAdapterVrModels(Context context, ArrayList<VrModel> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolderVrModels onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vr_model_item, parent, false);
        return new ViewHolderVrModels(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderVrModels holder, int position) {
        VrModel model = list.get(position);
        holder.image.setImageResource(model.img);

        // Set background color based on the selected position
        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bgClr_2)); // Change the color as needed
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE); // Reset to default
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

//viewholder for all the item elements and functions
class ViewHolderVrModels extends RecyclerView.ViewHolder{

//    TextView image, sinhala;
    ImageView image;
    CardView cardView;

    private RecycleViewAdapterVrModels adapter;

    public ViewHolderVrModels(@NonNull View itemView) {
        super(itemView);

//        number = itemView.findViewById(R.id.number);
//        sinhala = itemView.findViewById(R.id.sinhala);
        cardView = itemView.findViewById(R.id.model_bg);
        image = itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.selectedPosition = getAdapterPosition();

                if (adapter.itemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapter.itemClickListener.onItemClick(position);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    //linking the adapter
    public ViewHolderVrModels linkAdapter (RecycleViewAdapterVrModels adapter) {
        this.adapter = adapter;
        return this;
    }
}
