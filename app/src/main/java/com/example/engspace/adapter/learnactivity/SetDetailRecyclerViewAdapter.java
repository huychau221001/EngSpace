package com.example.engspace.adapter.learnactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.SetDetailResponse;

import java.util.ArrayList;

public class SetDetailRecyclerViewAdapter extends RecyclerView.Adapter<SetDetailRecyclerViewAdapter.SetDetailViewHolder> {
    private Context context;
    private ArrayList<SetDetailResponse> listSetDetail;
    OnItemClickCallback onItemClickCallback;
    public SetDetailRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<SetDetailResponse> list) {
        this.listSetDetail = list;
    }

    public ArrayList<SetDetailResponse> getData() {
        return listSetDetail;
    }

    @NonNull
    @Override
    public SetDetailRecyclerViewAdapter.SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_learn_card, parent, false);
        return new SetDetailRecyclerViewAdapter.SetDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetDetailRecyclerViewAdapter.SetDetailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SetDetailResponse set = listSetDetail.get(position);
        if (set == null) {
            return;
        }
        holder.tvTerm.setText(set.getTerm());
        holder.tvDefinition.setText(set.getDefinition());
        holder.btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.itemClickAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listSetDetail != null) {
            return listSetDetail.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class SetDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTerm;
        private TextView tvDefinition;
        private ImageView btnSpeak;

        public SetDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTerm = itemView.findViewById(R.id.term_textview);
            tvDefinition = itemView.findViewById(R.id.definition_textview);
            btnSpeak = itemView.findViewById(R.id.speak_icon);
        }
    }

    public interface OnItemClickCallback {
        void itemClickAt(int position);
    }
}
