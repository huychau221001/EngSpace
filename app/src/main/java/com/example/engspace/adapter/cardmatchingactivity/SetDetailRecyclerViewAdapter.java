package com.example.engspace.adapter.cardmatchingactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.SetDetailResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class SetDetailRecyclerViewAdapter extends RecyclerView.Adapter<SetDetailRecyclerViewAdapter.SetDetailViewHolder> {
    private Context context;
    private ArrayList<SetDetailResponse> listSetDetail;
    private ArrayList<Boolean> listSelected;
    OnItemClickCallback onItemClickCallback;
    public SetDetailRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
        this.listSelected = new ArrayList<Boolean>();
    }

    public void setData(ArrayList<SetDetailResponse> list) {
        this.listSetDetail = list;
        for (int i = 0; i < this.listSetDetail.size(); i++) {
            this.listSelected.add(false);
        }
        notifyDataSetChanged();
    }

    public void setBorder(int position) {
        this.listSelected.set(position, !this.listSelected.get(position));
        notifyDataSetChanged();
    }

    public void resetBorder() {
        this.listSelected = new ArrayList<>();
        for (int i = 0; i < this.listSetDetail.size(); i++) {
            this.listSelected.add(false);
        }
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.listSetDetail.remove(position);
        this.listSelected.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.listSetDetail.size());
        notifyItemRangeChanged(position, this.listSelected.size());
    }


    public ArrayList<SetDetailResponse> getData() {
        return listSetDetail;
    }

    @NonNull
    @Override
    public SetDetailRecyclerViewAdapter.SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_matching, parent, false);
        return new SetDetailRecyclerViewAdapter.SetDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetDetailRecyclerViewAdapter.SetDetailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SetDetailResponse set = listSetDetail.get(position);
        if (set == null) {
            return;
        }
        if (set.getType() == 0) {
            holder.cardText.setText(set.getTerm());
        } else {
            holder.cardText.setText(set.getDefinition());
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (2 * scale + 0.5f);
        if (listSelected.get(position)) {
            holder.cardView.setStrokeColor(ContextCompat.getColor(context, R.color.teal_200_adr));
            holder.cardView.setStrokeWidth(pixels);
        } else {
            holder.cardView.setStrokeWidth(0);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.itemClickAt(holder.getAdapterPosition());
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
        private TextView cardText;
        private MaterialCardView cardView;

        public SetDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_matching);
            cardText = itemView.findViewById(R.id.card_matching_text);
        }
    }
    public interface OnItemClickCallback {
        void itemClickAt(int position);
    }
}
