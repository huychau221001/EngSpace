package com.example.engspace.adapter.folderactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.UserResponse;
import com.google.android.material.card.MaterialCardView;

import java.text.DateFormat;
import java.util.ArrayList;

public class SetRecyclerViewAdapter extends RecyclerView.Adapter<SetRecyclerViewAdapter.SetViewHolder> {
    private Context context;
    private ArrayList<SetResponse> listSet;
    private ArrayList<Boolean> listSelected;
    OnItemClickCallback onItemClickCallback;

    public SetRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
        this.listSelected = new ArrayList<Boolean>();
    }

    public void setData(ArrayList<SetResponse> list) {
        this.listSet = list;
        for (int i = 0; i < this.listSet.size(); i++) {
            this.listSelected.add(false);
        }
        notifyDataSetChanged();
    }

    public void setBorder(int position) {
        this.listSelected.set(position, !this.listSelected.get(position));
        notifyDataSetChanged();
    }

    public void setBorderByArrayId(ArrayList<SetResponse> sets) {
        for (int i = 0; i < this.listSelected.size(); i++) {
            for (int j = 0; j < sets.size(); j++) {
                if (sets.get(j).getId() == this.listSet.get(i).getId()) {
                    this.listSelected.set(i, true);
                }
            }

        }
        notifyDataSetChanged();
    }

    public ArrayList<SetResponse> getData() {
        return listSet;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
        return new SetRecyclerViewAdapter.SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SetResponse set = listSet.get(position);
        if (set == null) {
            return;
        }
        holder.setAmount.setText(String.valueOf(set.getAmount()) + " thuật ngữ");
        holder.setTitle.setText(set.getName());
        holder.setUserPhoto.setImageResource(R.drawable.d1);
        UserResponse userResponse = set.getUser();
        String dateShort = DateFormat.getDateInstance(DateFormat.SHORT).format(set.getDate_created());
        holder.setUser.setText(userResponse.getUsername() + " | " + dateShort);
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (2 * scale + 0.5f);
        if (listSelected.get(position)) {
            holder.cardView.setStrokeColor(ContextCompat.getColor(context, R.color.es_main_blue));
            holder.cardView.setStrokeWidth(pixels);
        } else {
            holder.cardView.setStrokeWidth(0);
        }
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

    public class SetViewHolder extends RecyclerView.ViewHolder {
        private TextView setTitle;
        private TextView setUser;
        private ImageView setUserPhoto;
        private TextView setAmount;
        private MaterialCardView cardView;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.set);
            setTitle = itemView.findViewById(R.id.set_title);
            setUser = itemView.findViewById(R.id.set_user);
            setUserPhoto = itemView.findViewById(R.id.set_user_photo);
            setAmount = itemView.findViewById(R.id.set_amount);
            cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public interface OnItemClickCallback {
        void itemClickAt(int position);
    }
}
