package com.example.engspace.adapter.searchfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.UserResponse;

import java.util.ArrayList;

public class SetResultRecyclerViewAdapter extends RecyclerView.Adapter<SetResultRecyclerViewAdapter.SetViewHolder> {
    private Context context;
    private ArrayList<SetResponse> listSet;
    OnItemClickCallback onItemClickCallback;

    public SetResultRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<SetResponse> list) {
        this.listSet = list;
        notifyDataSetChanged();
    }

    public ArrayList<SetResponse> getData() {
        return listSet;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
        return new SetResultRecyclerViewAdapter.SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SetResponse set = listSet.get(position);
        if (set == null) {
            return;
        }
        holder.setAmount.setText(String.valueOf(set.getAmount()) + " thuật ngữ");
        holder.setTitle.setText(set.getName());
        UserResponse userResponse = set.getUser();
        holder.setUserPhoto.setImageResource(R.drawable.d1);
        holder.setUser.setText(userResponse.getUsername());
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
        private CardView cardView;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);

            setTitle = itemView.findViewById(R.id.set_title);
            setUser = itemView.findViewById(R.id.set_user);
            setUserPhoto = itemView.findViewById(R.id.set_user_photo);
            setAmount = itemView.findViewById(R.id.set_amount);
            cardView = itemView.findViewById(R.id.set);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public interface OnItemClickCallback {
        void itemClickAt(int position);
    }
}
