package com.example.engspace.adapter.searchfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.TopicResponse;

import java.util.ArrayList;

public class TopicRecyclerViewAdapter extends RecyclerView.Adapter<TopicRecyclerViewAdapter.TopicViewHolder> {
    private Context context;
    private ArrayList<TopicResponse> listTopic;
    OnItemClickCallback onItemClickCallback;
    public TopicRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<TopicResponse> list) {
        this.listTopic = list;
        notifyDataSetChanged();
    }
    public ArrayList<TopicResponse> getData() {
        return listTopic;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new TopicRecyclerViewAdapter.TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        TopicResponse topic = listTopic.get(position);
        if (topic == null) {
            return;
        }
        holder.topicTitle.setText(topic.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.itemClickAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listTopic != null) {
            return listTopic.size();
        }
        return 0;
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView topicTitle;
        private CardView cardView;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicTitle = itemView.findViewById(R.id.topic_title);
            cardView = itemView.findViewById(R.id.topic);
        }
    }

    public interface OnItemClickCallback {
        void itemClickAt(int position);
    }
}
