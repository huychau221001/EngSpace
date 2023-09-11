package com.example.engspace.adapter.homefragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.SetTable;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Random;

public class SetRecentRecyclerViewAdapter extends RecyclerView.Adapter<SetRecentRecyclerViewAdapter.SetRecentViewHolder> {
    private Context context;
    private ArrayList<SetTable> listSet;
    OnItemClickCallback onItemClickCallback;

    public SetRecentRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<SetTable> list) {
        this.listSet = list;
        notifyDataSetChanged();
    }

    public ArrayList<SetTable> getData() {
        return this.listSet;
    }

    @NonNull
    @Override
    public SetRecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_set, parent, false);
        return new SetRecentRecyclerViewAdapter.SetRecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetRecentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SetTable set = listSet.get(position);
        if (set == null) {
            return;
        }
        holder.recentSetTitle.setText(set.getName());
        holder.recentSetAmount.setText(set.getAmount() + " thuật ngữ");
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(125));
        holder.recentSetIcon.setColorFilter(color);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.itemClickAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listSet != null) {
            return listSet.size();
        }
        return 0;
    }

    public class SetRecentViewHolder extends RecyclerView.ViewHolder {
        private TextView recentSetTitle;
        private TextView recentSetAmount;
        private ImageView recentSetIcon;
        private MaterialCardView cardView;

        public SetRecentViewHolder(@NonNull View itemView) {
            super(itemView);
            recentSetTitle = itemView.findViewById(R.id.recent_set_title);
            recentSetAmount = itemView.findViewById(R.id.recent_set_amount);
            recentSetIcon = itemView.findViewById(R.id.recent_set_icon);
            cardView = itemView.findViewById(R.id.recent_set);
        }
    }

    public interface OnItemClickCallback {
        void itemClickAt(int position);
    }
}
