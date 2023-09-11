package com.example.engspace.adapter.setfragment;

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
import com.example.engspace.model.Set;

import java.util.List;

public class SetRecyclerViewAdapter extends RecyclerView.Adapter<SetRecyclerViewAdapter.SetViewHolder> {
    private Context context;
    private List<Set> listSet;

    public SetRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Set> list) {
        this.listSet = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
        return new SetRecyclerViewAdapter.SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Set set = listSet.get(position);
        if (set == null) {
            return;
        }
        holder.setAmount.setText(set.getAmount() + " thuật ngữ");
        holder.setTitle.setText(set.getTitle());
        holder.setUserPhoto.setImageResource(set.getUser_img());
        holder.setUser.setText(set.getUser());
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
        private CardView setCard;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);

            setTitle = itemView.findViewById(R.id.set_title);
            setUser = itemView.findViewById(R.id.set_user);
            setUserPhoto = itemView.findViewById(R.id.set_user_photo);
            setAmount = itemView.findViewById(R.id.set_amount);
            setCard = itemView.findViewById(R.id.set);
            setCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
