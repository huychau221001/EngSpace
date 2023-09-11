package com.example.engspace.adapter.homefragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.Banner;

import java.util.List;

public class BannerRecyclerViewAdapter extends RecyclerView.Adapter<BannerRecyclerViewAdapter.BannerViewHolder> {
    private Context context;
    private List<Banner> listBanner;

    public BannerRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Banner> list) {
        this.listBanner = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_quote, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = listBanner.get(position);
        if (banner == null) {
            return;
        }
        holder.titleTextView.setText(banner.getTitle());
        holder.contentTextView.setText(banner.getContent());
        holder.bannerImageView.setImageResource(banner.getImage());
        holder.cardView.setCardBackgroundColor(Color.parseColor(banner.getColor()));
    }

    @Override
    public int getItemCount() {
        if (listBanner != null) {
            return listBanner.size();
        }
        return 0;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView contentTextView;
        private ImageView bannerImageView;
        private CardView cardView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.banner_title);
            contentTextView = itemView.findViewById(R.id.banner_content);
            bannerImageView = itemView.findViewById(R.id.banner_image);
            cardView = itemView.findViewById(R.id.banner);
        }
    }
}
