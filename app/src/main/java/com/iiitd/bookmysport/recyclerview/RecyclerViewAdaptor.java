package com.iiitd.bookmysport.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iiitd.bookmysport.R;

import java.util.ArrayList;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder> {

    private Context context;
    private ArrayList<String> sport_names;
    private ArrayList<Integer> sport_images;
    private onListListener OnListListener;

    public RecyclerViewAdaptor(Context context, ArrayList<String> sport_names, ArrayList<Integer> sport_images, onListListener OnListListener) {
        this.OnListListener = OnListListener;
        this.sport_images = sport_images;
        this.sport_names = sport_names;
        this.context = context;
    }

    RecyclerViewAdaptor(Context context, ArrayList<String> sport_names, ArrayList<Integer> sport_images) {
        this.context = context;
        this.sport_names = sport_names;
        this.sport_images = sport_images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sport_list_layout, parent, false);
        return new ViewHolder(view, OnListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.sport_name.setBackgroundResource(sport_images.get(position));
        holder.sport_name.setText(sport_names.get(position));
    }

    @Override
    public int getItemCount() {
        return sport_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button sport_name;
        RelativeLayout list_parent_layout;
        onListListener OnListListener;

        ViewHolder(@NonNull View itemView, onListListener OnListListener) {
            super(itemView);
            sport_name = itemView.findViewById(R.id.Sport_List_Name);
            list_parent_layout = itemView.findViewById(R.id.list_parent_layout);
            this.OnListListener = OnListListener;
            sport_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            OnListListener.onListClick(getAdapterPosition());
        }

    }

    public interface onListListener {
        void onListClick(int pos);
    }
}